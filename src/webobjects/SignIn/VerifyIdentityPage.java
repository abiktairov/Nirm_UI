package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

public class VerifyIdentityPage extends SignInPage {
    private String by_verificationCode = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";
    private String by_resend_code_link = "//a[@id='resendInitialIdentityCode']";
    private String by_alert_code_sent = "//div[@id='alertblock']";
    private String text_alert_code_sent = "Successfully sent verification code";

    public VerifyIdentityPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_verifyIdentity), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public VerifyIdentityPage enterVerificationCode(String veridicationCode) {
        updateElement(by_verificationCode, veridicationCode);
        return this;
    }

    public WebPage clickSignInBtn() {
        clickElement(by_loginBtn);
        if (waitDisappear(by_login_title, text_verifyIdentity)) return dispatchClass();
        else return this;
    }

    public boolean clickResendCodeLink() {
        clickElement(by_resend_code_link);
        return waitAppear(by_alert_code_sent, text_alert_code_sent);
    }
}
