package webobjects;

import framework.TestData;
import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.SignIn.EnterPasswordPage;
import webobjects.SignIn.SelectAccountPage;
import webobjects.SignIn.VerifyIdentityPage;

public class SignInPage extends WebPage {
    protected String by_login_title = "//div[@class='login-title']";
    protected String text_login = "Sign in to Nirmata";
    protected String text_verifyIdentity = "Verify Identity";
    protected String text_selectAccount = "Select an Account";
    protected String text_enterPassword = "Hello, ";

    public SignInPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebPage dispatchClass() {
        String login_title_text = waitAppear(By.xpath(by_login_title)).getText();
        if (login_title_text.contains(text_verifyIdentity)) return new VerifyIdentityPage(webDriver);
        if (login_title_text.contains(text_selectAccount)) return new SelectAccountPage(webDriver);
        if (login_title_text.contains(text_enterPassword)) return new EnterPasswordPage(webDriver);
        return null;
    }

}
