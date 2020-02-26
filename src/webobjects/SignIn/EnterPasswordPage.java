package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.MainApplicationPage;
import webobjects.SignInPage;

public class EnterPasswordPage extends SignInPage {
    private By by_password = By.xpath("//input[@id='password']");
    private By by_loginBtn = By.xpath("//button[@id='btnLogin']");

    public EnterPasswordPage(WebDriver webDriver) {
        super(webDriver);
        waitAppear(By.xpath(by_login_title), text_enterPassword);
    }

    public EnterPasswordPage enterPassword(String password) {
        webDriver.findElement(by_password).sendKeys(password);
        return this;
    }

    public WebPage clickSignInBtn() {
        webDriver.findElement(by_loginBtn).click();
        waitDisappear(By.xpath(by_login_title), text_enterPassword);
        return new MainApplicationPage(webDriver).dispatchClass();
//        return new MainApplicationPage(webDriver).getWebPageClass();       // think about it!!!!!!!!!!!!!!
    }

}
