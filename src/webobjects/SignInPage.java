package webobjects;

import framework.TestData;
import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignIn.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SignInPage extends WebPage {
    protected String by_login_title = "//div[@class='login-title']";
    protected String text_login = "Sign in to Nirmata";
    protected String text_verifyIdentity = "Verify Identity";
    protected String text_selectAccount = "Select an Account";
    protected String text_enterPassword = "Hello, ";
    protected String text_signUp = "Sign up for a Free Trial";
    protected String text_resetPassword = "Reset your password";
    protected String text_selectAccountReset = "Select Account for Reset";
    protected String text_setPasswordPage = "Set your password";

//    protected String by_EnterEmailPage = "//div[@class='login-title' and contains(text(), 'Sign in to Nirmata')]";
//    protected String by_ResetPasswordPage = "//div[@class='login-block' and contains(., 'Reset your password') and contains(., 'Your request has been submitted')]";
//    protected String by_ResetPasswordConfirmationPage = "//div[@class='login-block' and contains(., 'Reset your password') and contains(., 'Your request has been submitted')]";

    public SignInPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public WebPage dispatchClass() {
        if (elementIsVisible(by_login_title)) {
            String login_title_text = getElement(by_login_title).getText();
            if (login_title_text.contains(text_login))              return new EnterEmailPage(webDriver);
            if (login_title_text.contains(text_verifyIdentity))     return new VerifyIdentityPage(webDriver);
            if (login_title_text.contains(text_selectAccount))      return new SelectAccountPage(webDriver);
            if (login_title_text.contains(text_enterPassword))      return new EnterPasswordPage(webDriver);
            if (login_title_text.contains(text_signUp))             return new SignUpPage(webDriver);
            if (login_title_text.contains(text_resetPassword))      return new ResetPasswordPage(webDriver);
            if (login_title_text.contains(text_selectAccountReset)) return new SelectAccountResetPage(webDriver);
            if (login_title_text.contains(text_setPasswordPage))    return new SetPasswordPage(webDriver);
        }
        return new MainApplicationPage(webDriver).dispatchClass();
    }

    @Override
    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_login_title), message);
        else             assertFalse(waitDisappear(by_login_title), message);
        return dispatchClass();
    }

}
