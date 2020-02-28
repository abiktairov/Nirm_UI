package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

public class SelectAccountPage extends SignInPage {
    private String by_selectAccount = "//*[contains(@class,'text-primary')]//*[.='__param__']";     // parametrized locator

    public SelectAccountPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_selectAccount), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public WebPage selectAccount(String login_account) {
       getElement(by_selectAccount.replaceAll("__param__", login_account)).click();
        if (waitDisappear(by_login_title, text_selectAccount)) return dispatchClass();
        return this;
    }

}
