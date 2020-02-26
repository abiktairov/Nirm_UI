package webobjects;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.MainApplication.GetStartedPage;
import webobjects.MainApplication.MainMenuPage;
import webobjects.MainApplication.TrialHasEndedPage;

public class MainApplicationPage extends WebPage {
    private String by_navbar_header = "//div[@class='navbar-header']";
    private String by_navbar_with_username = "//div[contains(@class,'navbar')]//span[contains(@class,'username')]";
    private String by_skip_setup = "//a[@id='skipSetup']";
    private String by_convert_to_tire = "//a[@id='convertToTire']";

    public MainApplicationPage(WebDriver webDriver) {super(webDriver);}

    public WebPage dispatchClass() {
        waitAppear(By.xpath(by_navbar_header));
        return elementIsVisible(By.xpath(by_navbar_with_username)) ? new MainMenuPage(webDriver) :
                elementIsVisible(By.xpath(by_skip_setup)) ? new GetStartedPage(webDriver) :
                        elementIsVisible(By.xpath(by_convert_to_tire)) ? new TrialHasEndedPage(webDriver) : null;
    }
}
