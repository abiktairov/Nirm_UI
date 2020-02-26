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

    public WebPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        wait = new WebDriverWait(webDriver, TIMEOUT, POLLING);
    }

    protected boolean elementIsVisible(By locator) {
        List<WebElement> webElements = webDriver.findElements(locator);
        return (webElements.size() > 0 && webElements.get(0).isDisplayed());
    }

    protected boolean elementIsVisible(By locator, String text) {
        List<WebElement> webElements = webDriver.findElements(locator);
        return (webElements.size() > 0 && webElements.get(0).isDisplayed() && webElements.get(0).getText().contains(text));
    }

    // checking just by a visibility of the element with the locator
    protected WebElement waitAppear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return webDriver.findElement(locator);
    }

    // checking by text presenting ('contains') in the element with the locator
    protected WebElement waitAppear(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        return webDriver.findElement(locator);
    }

    // checking by text presenting ('equals') in the element with the locator (if strict==true)
    protected WebElement waitAppear(By locator, String text, boolean strict) {
        if (strict) wait.until(ExpectedConditions.textToBe(locator, text));
        else wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        return webDriver.findElement(locator);
    }

    protected void waitDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitDisappear(By locator, String text) {
        // try to check invisibility of element or (if it visible) check text inside the element
        wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(locator),
                ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(locator, text))
        ));
    }

    protected void waitDisappear(By locator, String text, boolean strict) {
        if (strict) wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(locator),
                ExpectedConditions.not(ExpectedConditions.textToBe(locator, text))
        ));
        else wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(locator),
                ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(locator, text))
        ));
    }

    protected WebPage dispatchClass() {
        return this;
    }

//    public boolean isClass(String classname) {
//        return classname.equals(getClassName());
//    }
//    public boolean isClass(String classname) {
//        return this.getClass().getName().contains(classname);
//    }


//    private String getClassName() {
//        String classname = this.getClass().getName();
//        return classname.substring(classname.lastIndexOf('.')+1);
//    }
}
