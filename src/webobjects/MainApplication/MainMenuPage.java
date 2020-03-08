package webobjects.MainApplication;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.MainApplication.MainMenu.IdentityAndAccess.Users;
import webobjects.MainApplicationPage;

import org.testng.Assert;

import static org.testng.Assert.assertTrue;

public class MainMenuPage extends MainApplicationPage {
    String by_navbar_with_username = "//div[contains(@class,'navbar')]//span[contains(@class,'username')]";
//    String by_menu_item = "//div[contains(@class,'main-navigation')]//span[contains(text(),'__param__')]";
//String by_menu_item = "//li[@id='__param__']";
    String by_menu_item = "//span[contains(@class,'title') and contains(text(),'__param__')]/ancestor::a[*]";


    public MainMenuPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_navbar_with_username), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public WebPage selectMenuItem(String s) {
        String by_navbar_with_username = this.by_menu_item.replaceAll("__param__", s);

        if (waitAppear(by_navbar_with_username)) getElement(by_navbar_with_username).click();

        switch (s) {
            case "iam_menu": ;
            case "Users": return new Users(webDriver);
        }
        return null;
    }

}
