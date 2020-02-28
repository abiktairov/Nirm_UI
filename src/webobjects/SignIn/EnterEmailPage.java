package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;


public class EnterEmailPage extends SignInPage {
    private String by_email = "//input[@id='email']";
    private String by_loginBtn = "//button[@id='btnLogin']";

    public EnterEmailPage(WebDriver webDriver, String applicationURL) {
        super(webDriver);
//        webDriver.get(applicationURL);
        Assert.assertTrue(waitAppear(by_login_title, text_login), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public EnterEmailPage enterEmail(String email) {
        getElement(by_email).sendKeys(email);
        return this;
    }

    public WebPage clickSignInBtn() {
        getElement(by_loginBtn).click();
        // if locator deasppeared (email accepted) then return result of dispatching by paranet class
        if (waitDisappear(by_login_title, text_login)) return dispatchClass();
        // if locator still appears (email isn't accepted) then return the same class
        else return this;
    }

}
