package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.SignInPage;


public class EnterEmailPage extends SignInPage {
    private String by_email = "//input[@id='email']";
    private String by_loginBtn = "//button[@id='btnLogin']";

    public EnterEmailPage(WebDriver webDriver, String applicationURL) {
        super(webDriver);
        webDriver.get(applicationURL);
        waitAppear(By.xpath(by_login_title), text_login);       // if web page is not confirmed - class is not created
    }

    public EnterEmailPage enterEmail(String email) {
        webDriver.findElement(By.xpath(by_email)).sendKeys(email);
        return this;
    }

    public WebPage clickSignInBtn() {
        webDriver.findElement(By.xpath(by_loginBtn)).click();
        waitDisappear(By.xpath(by_login_title), text_login);
        return dispatchClass();
    }

}
