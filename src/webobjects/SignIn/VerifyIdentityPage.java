package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

public class VerifyIdentityPage extends SignInPage {
    private String by_verificationCode = "//input[@id='password']";
    private String by_loginBtn = "//button[@id='btnLogin']";

    public VerifyIdentityPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_verifyIdentity), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public VerifyIdentityPage enterVerificationCode(String veridicationCode) {
        webDriver.findElement(By.xpath(by_verificationCode)).sendKeys(veridicationCode);
        return this;
    }

    public WebPage clickSignInBtn() {
        getElement(by_loginBtn).click();
        if (waitDisappear(by_login_title, text_verifyIdentity)) return dispatchClass();
        else return this;
    }

}
