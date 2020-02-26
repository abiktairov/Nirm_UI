package classes;

import framework.JavaMailer;
import framework.WebPage;
import org.openqa.selenium.WebDriver;
import webobjects.SignIn.EnterPasswordPage;
import webobjects.SignIn.EnterEmailPage;
import framework.TestData;
import webobjects.SignIn.SelectAccountPage;
import webobjects.SignIn.VerifyIdentityPage;

import java.util.Date;


public class SignIn {
    private Date loginStartTime;
    private WebPage nextPage;

    public SignIn(WebDriver webDriver, String applicationURL) {
        loginStartTime = new Date();
        nextPage = new EnterEmailPage(webDriver,applicationURL + TestData.getProperty("SignIn_URI"));
    }

    public WebPage enterEmail(String login_email) {
        return ((EnterEmailPage) nextPage).enterEmail(login_email).clickSignInBtn();
    }

    public WebPage verifyIdentity(String verificationCode) {
        return ((VerifyIdentityPage) nextPage).enterVerificationCode(verificationCode).clickSignInBtn();
    }

    public WebPage selectAccount(String login_account) {
        return ((SelectAccountPage) nextPage).selectAccount(login_account);
    }

    public WebPage enterPassword(String userPassword) {
        return ((EnterPasswordPage) nextPage).enterPassword(userPassword).clickSignInBtn();
    }

    public WebPage loginNirmataAccount(String login_email, String login_account) {
        nextPage = enterEmail(login_email);
        nextPage = nextPage instanceof VerifyIdentityPage ? verifyIdentity(extractCodeFromEmail(login_email)) : nextPage;
        nextPage = nextPage instanceof SelectAccountPage ? selectAccount(login_account) : nextPage;
        return nextPage instanceof EnterPasswordPage ? enterPassword(TestData.getUser("user_password", login_email)) : nextPage;
    }

    private String extractCodeFromEmail(String login_email) {
        JavaMailer javaMailer = new JavaMailer(TestData.getUser("email_host", login_email),
                TestData.getUser("email_protocol", login_email),
                TestData.getUser("email_folder", login_email),
                TestData.getUser("email", login_email),
                TestData.getUser("email_password", login_email));
        return javaMailer.getAccessCode(loginStartTime, 300000);
    }

}
