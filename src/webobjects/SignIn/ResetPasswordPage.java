package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

public class ResetPasswordPage extends SignInPage {
    private String by_email = "//input[@id='email']";
    private String by_resetBtn = "//button[contains(.,'Reset Password')]";
    private String by_confirmation_text = "//*[contains(text(),'Your request has been submitted.')]";


    public ResetPasswordPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_resetPassword), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public ResetPasswordPage enterEmail(String email) {
        getElement(by_email).clear();
        getElement(by_email).sendKeys(email);
        return this;
    }

    public WebPage clickResetPasswordBtn() {
        getElement(by_resetBtn).click();
        return this;
    }

    public boolean isResetConfirmationAppeared() {
        return waitDisappear(by_resetBtn) && waitAppear(by_confirmation_text);
    }
}
