package framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WebPage {
    private static final int LONG_TIMEOUT = 30;     // using for expected appearing (may take longer time)
    private static final int SHORT_TIMEOUT = 5;     // using for expected disappearing (usually shorter)
    private static final int POLLING = 1000;
    protected WebDriver webDriver;
    private WebDriverWait longWait, shortWait;

    public WebPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        longWait = new WebDriverWait(webDriver, LONG_TIMEOUT, POLLING);
        shortWait = new WebDriverWait(webDriver, SHORT_TIMEOUT, POLLING);
    }

    protected boolean elementIsVisible(String xpath) {
        List<WebElement> webElements = webDriver.findElements(By.xpath(xpath));
        return (webElements.size() > 0 && webElements.get(0).isDisplayed());
    }

    protected boolean elementIsVisible(String xpath, String text) {
        List<WebElement> webElements = webDriver.findElements(By.xpath(xpath));
        return (webElements.size() > 0 && webElements.get(0).isDisplayed() && webElements.get(0).getText().contains(text));
    }

    // checking just by a visibility of the element with the locator
    protected boolean waitAppear(String xpath) {
        try { longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))); }
        catch (Exception e) { TestSetup.lastException = e; return false; }
        return true;
    }

    // checking by text presenting ('contains') in the element with the locator
    protected boolean waitAppear(String xpath, String text) {
        try { longWait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text)); }
        catch (Exception e) { TestSetup.lastException = e; return false; }
        return true;
    }

    // checking by text presenting ('equals') in the element with the locator (if strict==true)
//    protected WebElement waitAppear(By locator, String text, boolean strict) {
//        if (strict) wait.until(ExpectedConditions.textToBe(locator, text));
//        else wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
//        return webDriver.findElement(locator);
//    }

    protected boolean waitDisappear(String xpath) {
        try { shortWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath))); }
        catch (Exception e) { TestSetup.lastException = e; return false; }
        return true;
    }

    protected boolean waitDisappear(String xpath, String text) {
        // try to check invisibility of element or (if it visible) check text inside the element
        try {
            shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)),
                    ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text))
            ));
        } catch (Exception e) { TestSetup.lastException = e; return false; }
        return true;
    }

    protected void waitDisappear(String xpath, String text, boolean strict) {
        if (strict) shortWait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)),
                ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath(xpath), text))
        ));
        else shortWait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)),
                ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text))
        ));
    }

    public WebElement getElement(String xpath) {
        return webDriver.findElement(By.xpath(xpath));
    }

    public WebElement updateElement(String xpath, String value) {
        WebElement webElement = getElement(xpath);
        webElement.clear();
        webElement.sendKeys(value);
        return webElement;
    }

    public WebElement clickElement(String xpath) {
        WebElement webElement = getElement(xpath);
        webElement.click();
        return webElement;
    }

    protected WebPage dispatchClass() {
        return this;
    }

//    public void forceLogout(String applicationURL) {
//        webDriver.get(applicationURL + TestData.getProperty("Logout_URI"));
//    }

}
