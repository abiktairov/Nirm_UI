package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.MainApplicationPage;
import webobjects.SignInPage;

import java.util.Collection;

public class EnterPasswordPage extends SignInPage {
    private String by_password = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";
    private String by_help_block = "//p[@class='help-block']";
    private String by_form_error = "//p[@id='form-errors']";
    private String by_forgot_your_password_link = "//a[contains(text(),'forgot your password?')]";


    public EnterPasswordPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_enterPassword), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public EnterPasswordPage enterPassword(String password) {
        updateElement(by_password, password);
        return this;
    }

    public WebPage clickSignInBtn() {
        clickElement(by_loginBtn);
//        if (waitDisappear(by_login_title, text_enterPassword)) return dispatchClass();
        if (waitDisappear(by_login_title, text_enterPassword)) return new MainApplicationPage(webDriver).dispatchClass();
        else return this;
    }

    public String getErrorMessage() {
        return elementIsVisible(by_help_block) ? getElement(by_help_block).getText() :
                elementIsVisible(by_form_error) ? getElement(by_form_error).getText() : "";
    }

    public WebPage clickForgotYourPasswordLink() {
        clickElement(by_forgot_your_password_link);
        if (waitDisappear(by_login_title, text_enterPassword)) return dispatchClass();
        return this;
    }
}
