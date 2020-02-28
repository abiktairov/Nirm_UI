package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.MainApplicationPage;
import webobjects.SignInPage;

public class EnterPasswordPage extends SignInPage {
    private String by_password = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";

    public EnterPasswordPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_enterPassword), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public EnterPasswordPage enterPassword(String password) {
        getElement(by_password).sendKeys(password);
        return this;
    }

    public WebPage clickSignInBtn() {
        getElement(by_loginBtn).click();
        if (waitDisappear(by_login_title, text_enterPassword)) return new MainApplicationPage(webDriver).dispatchClass();
        return this;
    }

}
