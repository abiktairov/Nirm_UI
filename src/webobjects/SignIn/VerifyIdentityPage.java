package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.SignInPage;

public class VerifyIdentityPage extends SignInPage {
    private String by_verificationCode = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";

    public VerifyIdentityPage(WebDriver webDriver) {
        super(webDriver);
        waitAppear(By.xpath(by_login_title), text_verifyIdentity);
    }

    public VerifyIdentityPage enterVerificationCode(String veridicationCode) {
        webDriver.findElement(By.xpath(by_verificationCode)).sendKeys(veridicationCode);
        return this;
    }

    public WebPage clickSignInBtn() {
        webDriver.findElement(By.xpath(by_loginBtn)).click();
        waitDisappear(By.xpath(by_login_title), text_verifyIdentity);
        return dispatchClass();
    }

}
