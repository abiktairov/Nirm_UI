package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

// verified for v.2.0
public class EnterPasswordPage extends SignInPage {
    private String by_password = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";
    private String by_help_block = "//p[@class='help-block']";
    private String by_form_error = "//p[@id='form-errors']";
    private String by_forgot_your_password_link = "//a[contains(text(),'forgot your password?')]";


    public EnterPasswordPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title, text_enterPassword), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public EnterPasswordPage enterPassword(String password) {
        updateElement(by_password, password);
        return this;
    }

    public EnterPasswordPage clickSignInBtn() {
        clickElement(by_loginBtn);
        return this;

    }

    public String getErrorMessage() {
        return elementIsVisible(by_help_block) ? getElement(by_help_block).getText() :
                elementIsVisible(by_form_error) ? getElement(by_form_error).getText() : "";
    }

    public EnterPasswordPage clickForgotYourPasswordLink() {
        clickElement(by_forgot_your_password_link);
        return this;
    }

    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_login_title, text_enterPassword), message);
        else             assertFalse(waitDisappear(by_login_title, text_enterPassword), message);
        return dispatchClass();
    }

}
