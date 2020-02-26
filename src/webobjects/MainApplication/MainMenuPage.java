package webobjects.MainApplication;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.MainApplication.MainMenu.IdentityAndAccess.Users;
import webobjects.MainApplicationPage;

public class MainMenuPage extends MainApplicationPage {
    private By by_navbar_with_username = By.xpath("//div[contains(@class,'navbar')]//span[contains(@class,'username')]");
//    String by_menu_item = "//div[contains(@class,'main-navigation')]//span[contains(text(),'__param__')]";
//String by_menu_item = "//li[@id='__param__']";
    String by_menu_item = "//span[contains(@class,'title') and contains(text(),'__param__')]/ancestor::a[*]";


    public MainMenuPage(WebDriver webDriver) {
        super(webDriver);
        waitAppear(by_navbar_with_username);
    }

    public WebPage selectMenuItem(String s) {
        By by = By.xpath(by_menu_item.replaceAll("__param__", s));

         waitAppear(by).click();

        switch (s) {
            case "iam_menu": ;
            case "Users": return new Users(webDriver);
        }
        return null;
    }
}
