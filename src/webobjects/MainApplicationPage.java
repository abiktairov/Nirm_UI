package webobjects;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.MainApplication.GetStartedPage;
import webobjects.MainApplication.MainMenuPage;
import webobjects.MainApplication.TrialHasEndedPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class MainApplicationPage extends WebPage {
    private String by_navbar_header = "//div[@class='navbar-header']";
    private String by_navbar_with_username = "//div[contains(@class,'navbar')]//span[contains(@class,'username')]";
    private String by_skip_setup = "//a[@id='skipSetup']";
    private String by_convert_to_tire = "//a[@id='convertToTire']";

    public MainApplicationPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_navbar_header), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public WebPage dispatchClass() {
        if (elementIsVisible(by_navbar_with_username))
            return new MainMenuPage(webDriver);
        else if (elementIsVisible(by_skip_setup))
            return new GetStartedPage(webDriver);
        else if (elementIsVisible(by_convert_to_tire))
            return new TrialHasEndedPage(webDriver);
        return null;
    }

    @Override
    public WebPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_navbar_header), message);
        else             assertFalse(waitDisappear(by_navbar_header), message);
        return dispatchClass();
    }
}
