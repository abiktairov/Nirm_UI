package classes;

import framework.NirmataMailer;
import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignIn.EnterPasswordPage;
import webobjects.SignIn.EnterEmailPage;
import framework.TestData;
import webobjects.SignIn.SelectAccountPage;
import webobjects.SignIn.VerifyIdentityPage;

import java.util.Date;


public class SignIn {
    private WebDriver webDriver;
    private String applicationURL;
    private Date loginStartTime;
    private WebPage nextPage;

    public SignIn(WebDriver webDriver, String applicationURL) {
        this.webDriver = webDriver;
        this.applicationURL = applicationURL;
        loginStartTime = new Date();
        webDriver.get(applicationURL);
        nextPage = new EnterEmailPage(webDriver);
    }

    public WebPage getNextPage() {
        return nextPage;
    }

    public EnterEmailPage forceLogout() {
        webDriver.get(applicationURL + TestData.getProperty("Logout_URI"));
        return new EnterEmailPage(webDriver);
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
        Assert.assertFalse(nextPage instanceof EnterEmailPage, "Email has not been accepted.");
        if (nextPage instanceof VerifyIdentityPage) {
            nextPage = verifyIdentity(extractCodeFromEmail(login_email, loginStartTime));
            Assert.assertFalse(nextPage instanceof VerifyIdentityPage, "Access code has not been accepted.");
        }
        if (nextPage instanceof SelectAccountPage) {
            nextPage = selectAccount(login_account);
            Assert.assertFalse(nextPage instanceof SelectAccountPage, "Account selection was failed.");
        }
        if (nextPage instanceof EnterPasswordPage) {
            nextPage = enterPassword(TestData.getUser("user_password", login_email));
            Assert.assertFalse(nextPage instanceof EnterPasswordPage, "Password has not been accepted.");
        }
        return nextPage;
    }

    // need to pass password when testing password changeing ------ need to optimize using later!
    public WebPage loginNirmataAccount(String login_email, String login_account, String password) {
        nextPage = enterEmail(login_email);
        Assert.assertFalse(nextPage instanceof EnterEmailPage, "Email has not been accepted.");
        if (nextPage instanceof VerifyIdentityPage) {
            nextPage = verifyIdentity(extractCodeFromEmail(login_email, loginStartTime));
            Assert.assertFalse(nextPage instanceof VerifyIdentityPage, "Access code has not been accepted.");
        }
        if (nextPage instanceof SelectAccountPage) {
            nextPage = selectAccount(login_account);
            Assert.assertFalse(nextPage instanceof SelectAccountPage, "Account selection was failed.");
        }
        if (nextPage instanceof EnterPasswordPage) {
            nextPage = enterPassword(password);
            Assert.assertFalse(nextPage instanceof EnterPasswordPage, "Password has not been accepted.");
        }
        return nextPage;
    }



    public String extractCodeFromEmail(String login_email, Date checkAfter) {
        NirmataMailer nirmataMailer = new NirmataMailer(TestData.getUser("email_host", login_email),
                TestData.getUser("email_protocol", login_email),
                TestData.getUser("email_folder", login_email),
                TestData.getUser("email", login_email),
                TestData.getUser("email_password", login_email));
        return nirmataMailer.getAccessCode(checkAfter, 60, 5);
    }

}
