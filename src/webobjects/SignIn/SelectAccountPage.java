package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

// verified for v.2.0
public class SelectAccountPage extends SignInPage {
    private String by_selectAccount = "//*[contains(@class,'text-primary')]//*[.='__param__']";     // parametrized locator
    private String by_another_email_link = "//a[@id='showEmailForm']";


    public SelectAccountPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title, text_selectAccount), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public SelectAccountPage selectAccount(String login_account) {
        String by_selectAccount = this.by_selectAccount.replaceAll("__param__", login_account);
        assertTrue (elementIsVisible(by_selectAccount), "Account " + login_account + " is not available for the user.");
        clickElement(by_selectAccount.replaceAll("__param__", login_account));
        return this;
    }

    public SelectAccountPage clickUseDifferentEmailLink() {
        clickElement(by_another_email_link);
        return this;
    }

    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_login_title, text_selectAccount), message);
        else             assertFalse(waitDisappear(by_login_title, text_selectAccount), message);
        return dispatchClass();
    }
}
