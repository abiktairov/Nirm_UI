package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;


public class EnterEmailPage extends SignInPage {
    private String by_email = "//input[@id='email']";
    private String by_remember_me = "//input[@id='rememberMe']";
    private String by_loginBtn = "//button[@id='btnLogin']";
    private String by_signup_link = "//a[@id='showSignup']";
    private String by_help_block = "//p[@class='help-block']";
    private String by_form_error = "//p[@id='form-errors']";

    public EnterEmailPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_login), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public EnterEmailPage enterEmail(String email) {
        getElement(by_email).clear();
        getElement(by_email).sendKeys(email);
        return this;
    }

    public String getEmail() {
        return getElement(by_email).getAttribute("value");
    }


    public EnterEmailPage clickRememberMe() {
        getElement(by_remember_me).click();
        return this;
    }

    public WebPage clickSignUpLink() {
        getElement(by_signup_link).click();
        if (waitDisappear(by_login_title, text_login)) return dispatchClass();
        else return this;
    }

    public WebPage clickSignInBtn() {
        getElement(by_loginBtn).click();
        // if locator deasppeared (email accepted) then return result of dispatching by paranet class
        if (waitDisappear(by_login_title, text_login)) return dispatchClass();
        // if locator still appears (email isn't accepted) then return the same class
        else return this;
    }

    public String getErrorMessage() {
        return elementIsVisible(by_help_block) ? getElement(by_help_block).getText() :
                elementIsVisible(by_form_error) ? getElement(by_form_error).getText() : "";
    }

}
