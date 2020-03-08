package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

// verified for v.2.0
public class SetPasswordPage extends SignInPage {
    private String by_password = "//input[@id='password']";
    private String by_confirm_password = "//input[@id='confirmPassword']";
    private String by_resetBtn = "//button[contains(.,'Reset Password')]";
    private String by_confirmation_text = "//div[@class='login-block']//p[contains(text(),'Your password has been reset.')]";
    private String by_signInBtn = "//button[contains(.,'SIGN IN TO NIRMATA')]";
    private String by_help_block_password = "//input[@id='password']/../..//p[@class='help-block']";
    private String by_help_block_confirmation = "//input[@id='confirmPassword']/../..//p[@class='help-block']";
    private String by_form_error = "//p[@id='form-errors']";

    public SetPasswordPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title, text_setPasswordPage), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public SetPasswordPage enterPassword(String password) {
        updateElement(by_password, password);
        return this;
    }

    public SetPasswordPage enterPasswordConfirmation(String password) {
        updateElement(by_confirm_password, password);
        return this;
    }

    public SetPasswordPage clicResetPasswordBtn() {
        clickElement(by_resetBtn);
        return this;
    }

    public SetPasswordPage clicSignInBtn() {
        clickElement(by_signInBtn);
        return this;
    }

    public SetPasswordPage assertPasswordConfirmation(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_resetBtn) && waitAppear(by_confirmation_text), message);
        else             assertFalse(waitDisappear(by_resetBtn) && waitAppear(by_confirmation_text), message);
        return this;
    }

    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_login_title, text_setPasswordPage), message);
        else             assertFalse(waitDisappear(by_login_title, text_setPasswordPage), message);
        return dispatchClass();
    }

    public boolean isSetPasswordConfirmationAppeared() {
        return waitDisappear(by_resetBtn) && waitAppear(by_confirmation_text);
    }

    public String getPasswordErrorMessage() {
        return elementIsVisible(by_help_block_password) ? getElement(by_help_block_password).getText() : "";
    }

    public String getConfirmationErrorMessage() {
        return elementIsVisible(by_help_block_confirmation) ? getElement(by_help_block_confirmation).getText() : "";
    }


}
