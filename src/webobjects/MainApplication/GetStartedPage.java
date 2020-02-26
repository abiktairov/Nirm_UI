package webobjects.MainApplication;

import framework.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import webobjects.MainApplicationPage;

public class GetStartedPage extends MainApplicationPage {
    private By by_get_started = By.xpath("//*[@id='quick-start-subtitle']");
    private By by_skipSetup = By.xpath("//a[@id='skipSetup']");
    private String text_get_started = "Let's get started...";

    public GetStartedPage(WebDriver webDriver) {
        super(webDriver);
        waitAppear(by_get_started, text_get_started);
    }

    public WebPage skipSetup()
    {
        webDriver.findElement(by_skipSetup).click();
        waitDisappear(by_get_started, text_get_started);
        return dispatchClass();
    }

}
