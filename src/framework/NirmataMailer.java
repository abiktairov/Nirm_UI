package framework;

import javax.mail.*;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NirmataMailer {
    private Session session;
    private String host, protocol, email, password, folderName;
    public Exception exception;

    // inner class to keep a short form of mail messages
    private static class ShortMessage {
        public Date sentDate;
        public String messageBody;
        public ShortMessage(Date sentDate, String messageBody) {
            this.sentDate = sentDate; this.messageBody = messageBody;
        }
    }

    // smart constructor working with TestData, need just email as an parameter
    public NirmataMailer(String email) {
        this(TestData.getUser("email_host", email),
                TestData.getUser("email_protocol", email),
                TestData.getUser("email_folder", email),
                TestData.getUser("email", email),
                TestData.getUser("email_password", email));
    }

    // full constructor taking many parameters
    public NirmataMailer(String host, String protocol, String folderName, String email, String password) {
        this.host = host;
        this.protocol = protocol;
        this.email = email;
        this.password = password;
        this.folderName = folderName;
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", protocol);
        session = Session.getDefaultInstance(props, null);
    }

    public String getAccessCode(Date testStartTime, int waitingTime_sec, int forcingTime_sec) {
        String checkFrom = "support@nirmata.com";
        String checkSubj = "Your Nirmata Account";
        String pattern = "(?<=Access code:)?([0-9]{6})";
        return getPatternFromEmail(checkFrom, checkSubj, pattern, testStartTime, waitingTime_sec, forcingTime_sec);
    }

    public String getPasswordResetLink(String appURL, Date testStartTime, int waitingTime_sec, int forcingTime_sec) {
        String checkFrom = "support@nirmata.com";
        String checkSubj = "Your Nirmata Account";
        String query = appURL + "security/setPassword.html?action=reset&token=";
        String pattern = "(?<=" + query + ")?(\\w{144})";
        return query + getPatternFromEmail(checkFrom, checkSubj, pattern, testStartTime, waitingTime_sec, forcingTime_sec);
    }

    public String getActivationLink(String appURL, Date testStartTime, int waitingTime_sec, int forcingTime_sec) {
        String checkFrom = "support@nirmata.com";
        String checkSubj = "Welcome to Nirmata!";
        String query = appURL + "security/setPassword.html?action=activate&token=";
        String pattern = "(?<=" + query + ")?(\\w{144})";
        return query + getPatternFromEmail(checkFrom, checkSubj, pattern, testStartTime, waitingTime_sec, forcingTime_sec);
    }

    private String getPatternFromEmail (String checkFrom, String checkSubj, String pattern, Date testStartTime, int waitingTime_sec, int forcingTime_sec) {
        final long TIMESHIFT = 2000; // to smooth out a possible bias between test host and mail host
        ArrayList<ShortMessage> messages;
        boolean found;
        String result = "";

        do {
            messages = getMessagesBy(checkFrom, checkSubj);
            found = (messages.size() > 0 && messages.get(0).sentDate.getTime() > testStartTime.getTime() - TIMESHIFT);
            if (!found) try {Thread.sleep(waitingTime_sec * 100);} catch (InterruptedException e) {e.printStackTrace();}
        } while (!found && testStartTime.getTime() + waitingTime_sec * 1000 > new Date().getTime());

        if (messages.size() > 0 &&
                (found || (
                        forcingTime_sec > 0 &&
                                messages.get(0).sentDate.getTime() > testStartTime.getTime() - forcingTime_sec * 1000 - TIMESHIFT)))
            result = parseMessage(messages.get(0).messageBody, pattern);

        return result;
    }


    private ArrayList<ShortMessage> getMessagesBy (String checkFrom, String checkSubj) {
        Store store;
        Folder folder;
        Message[] messages;
        ArrayList<ShortMessage> result = new ArrayList<>();

        try {
            store = session.getStore(protocol);
            store.connect(host, email, password);
            folder = store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);
            messages = folder.search(new SubjectTerm(checkSubj), folder.search(new FromStringTerm(checkFrom)));
            for (int i = messages.length - 1; i >= 0; i--) {
                ShortMessage shortMessage = new ShortMessage(messages[i].getSentDate(), messages[i].getContent().toString());
                result.add(shortMessage);
                // delete messages sent more then 1 hour ago
                if (shortMessage.sentDate.getTime() < new Date().getTime() - 3600000)
                    messages[i].setFlag(Flags.Flag.DELETED, true);
            }
            folder.close(true);
            store.close();
        } catch (MessagingException | IOException e) {this.exception = e;}

        return result;
    }

    private String parseMessage(String message, String pattern) {
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(message);
        String result = "";
        while (ma.find()) result = message.substring(ma.start(), ma.end());
        return result;
    }

}
