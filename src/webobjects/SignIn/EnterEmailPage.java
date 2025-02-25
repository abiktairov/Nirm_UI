package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

// verified for v.2.0
public class EnterEmailPage extends SignInPage {
    private String by_email = "//input[@id='email']";
    private String by_remember_me = "//input[@id='rememberMe']";
    private String by_loginBtn = "//button[@id='btnLogin']";
    private String by_signup_link = "//a[@id='showSignup']";
    private String by_help_block = "//p[@class='help-block']";
    private String by_form_error = "//p[@id='form-errors']";

    public EnterEmailPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title, text_login), "Timeout of pageObject " + this.getClass().getName() + " appearing.");
    }

    public EnterEmailPage enterEmail(String email) {
        updateElement(by_email, email);
        return this;
    }

    public String getEmail() {
        return getElement(by_email).getAttribute("value");
    }


    public EnterEmailPage clickRememberMe() {
        getElement(by_remember_me).click();
        return this;
    }

    public EnterEmailPage clickSignUpLink() {
        clickElement(by_signup_link);
        return this;
    }

    public EnterEmailPage clickSignInBtn() {
        clickElement(by_loginBtn);
        return this;
    }

    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_login_title, text_login), message);
        else             assertFalse(waitDisappear(by_login_title, text_login), message);
        return dispatchClass();
    }

    public String getErrorMessage() {
        return elementIsVisible(by_help_block) ? getElement(by_help_block).getText() :
                elementIsVisible(by_form_error) ? getElement(by_form_error).getText() : "";
    }

}
