package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

public class SelectAccountResetPage extends SignInPage {
    private String by_selectAccount = "//*[contains(@class,'text-primary')]//*[.='__param__']";     // parametrized locator


    public SelectAccountResetPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_selectAccountReset), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public WebPage selectAccount(String login_account) {
        clickElement(by_selectAccount.replaceAll("__param__", login_account));
        if (waitDisappear(by_login_title, text_selectAccountReset)) return dispatchClass();
        return this;
    }
}
