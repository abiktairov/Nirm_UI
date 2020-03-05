package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

// verified for v.2.0
public class SelectAccountResetPage extends SignInPage {
    private String by_selectAccount = "//*[contains(@class,'text-primary')]//*[.='__param__']";     // parametrized locator

    public SelectAccountResetPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_selectAccountReset), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public SelectAccountResetPage selectAccount(String login_account) {
        clickElement(by_selectAccount.replaceAll("__param__", login_account));
        return this;
    }

    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) Assert.assertTrue(waitDisappear(by_login_title, text_selectAccountReset), message);
        else             Assert.assertFalse(waitDisappear(by_login_title, text_selectAccountReset), message);
        return dispatchClass();
    }
}
