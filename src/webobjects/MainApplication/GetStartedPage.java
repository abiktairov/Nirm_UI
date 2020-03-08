package webobjects.MainApplication;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.MainApplicationPage;

import static org.testng.Assert.assertTrue;

public class GetStartedPage extends MainApplicationPage {
    private String by_get_started = "//*[@id='quick-start-subtitle']";
    private String by_skipSetup = "//a[@id='skipSetup']";
    private String text_get_started = "Let's get started...";

    public GetStartedPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_get_started, text_get_started), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public WebPage skipSetup()
    {
        getElement(by_skipSetup).click();
        if (waitDisappear(by_get_started, text_get_started)) return dispatchClass();
        return this;
    }

}
