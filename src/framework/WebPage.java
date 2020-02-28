package framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WebPage {
    private static final int TIMEOUT = 30;
    private static final int POLLING = 1000;
    protected WebDriver webDriver;
    private WebDriverWait wait;
    protected Exception exception;

    public WebPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        wait = new WebDriverWait(webDriver, TIMEOUT, POLLING);
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
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))); }
        catch (Exception e) { exception=e; return false; }
        return true;
    }

    // checking by text presenting ('contains') in the element with the locator
    protected boolean waitAppear(String xpath, String text) {
        try { wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text)); }
        catch (Exception e) { exception = e; return false; }
        return true;
    }

    // checking by text presenting ('equals') in the element with the locator (if strict==true)
//    protected WebElement waitAppear(By locator, String text, boolean strict) {
//        if (strict) wait.until(ExpectedConditions.textToBe(locator, text));
//        else wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
//        return webDriver.findElement(locator);
//    }

    protected boolean waitDisappear(String xpath) {
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath))); }
        catch (Exception e) { exception = e; return false; }
        return true;
    }

    protected boolean waitDisappear(String xpath, String text) {
        // try to check invisibility of element or (if it visible) check text inside the element
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)),
                    ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text))
            ));
        } catch (Exception e) { exception = e; return false; }
        return true;
    }

    protected void waitDisappear(String xpath, String text, boolean strict) {
        if (strict) wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)),
                ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath(xpath), text))
        ));
        else wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)),
                ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text))
        ));
    }

    public WebElement getElement(String xpath) {
        return webDriver.findElement(By.xpath(xpath));
    }
    protected WebPage dispatchClass() {
        return this;
    }

    public void forceLogout(String applicationURL) {
        webDriver.get(applicationURL + TestData.getProperty("Logout_URI"));
    }

}
