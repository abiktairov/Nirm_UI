package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

// verified for v.2.0
public class VerifyIdentityPage extends SignInPage {
    private String by_verificationCode = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";
    private String by_resend_code_link = "//a[@id='resendInitialIdentityCode']";
    private String by_alert_code_sent = "//div[@id='alertblock']";
    private String text_alert_code_sent = "Successfully sent verification code";

    public VerifyIdentityPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title, text_verifyIdentity), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public VerifyIdentityPage enterVerificationCode(String veridicationCode) {
        updateElement(by_verificationCode, veridicationCode);
        return this;
    }

    public VerifyIdentityPage clickSignInBtn() {
        clickElement(by_loginBtn);
        return this;
    }

    public boolean clickResendCodeLink() {
        clickElement(by_resend_code_link);
        return waitAppear(by_alert_code_sent, text_alert_code_sent);
    }

    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_login_title, text_verifyIdentity), message);
        else             assertFalse(waitDisappear(by_login_title, text_verifyIdentity), message);
        return dispatchClass();
    }
}
