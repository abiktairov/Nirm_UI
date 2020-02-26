package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.SignInPage;

public class SelectAccountPage extends SignInPage {
    private String by_selectAccount = "//*[contains(@class,'text-primary')]//*[.='__param__']";     // parametrized locator

    public SelectAccountPage(WebDriver webDriver) {
        super(webDriver);
        waitAppear(By.xpath(by_login_title), text_selectAccount);
    }

    public WebPage selectAccount(String login_account) {

        webDriver.findElement(By.xpath(by_selectAccount.replaceAll("__param__", login_account))).click();
        waitDisappear(By.xpath(by_login_title), text_selectAccount);
        return dispatchClass();
    }

}
