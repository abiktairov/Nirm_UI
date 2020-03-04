package framework;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import webobjects.SignIn.EnterEmailPage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestSetup {
    protected static EventFiringWebDriver webDriver;
    public static Date testStartTime = new Date();
    public static Exception lastException;
    protected String applicationURL = TestData.getProperty("App_URL");
    private static boolean _headless, _start_maximized;;
    private static String _browser, _window_position, _window_size, _os;
    private static String reportDir = TestData.getProperty("Report_Directory");
    protected SoftAssert softAssert = new SoftAssert();

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports reports;
    public static ExtentTest suiteInfo;
    public static ExtentTest methodInfo;
    public static ExtentTest testInfo;
    public static String testName;

    @BeforeSuite
    @Parameters ({"browser", "headless", "window_position", "window_size", "start_maximized"})
    public static void setUp (@Optional String browser, @Optional String headless, @Optional String window_position, @Optional String window_size, @Optional String start_maximized) {

        // initialize optional variables
        _headless = (headless != null) && headless.equalsIgnoreCase("true");
        _browser = (browser != null) ? browser : "Chrome";
        _window_position = window_position;
        _window_size = window_size;
        _start_maximized = (start_maximized != null) && start_maximized.equalsIgnoreCase("true");
        _os = System.getProperty("os.name").toLowerCase();

        // initialize report system

        String reportName = reportDir + "//" + new SimpleDateFormat("yyyyMMddhhmmss").format(testStartTime) + ".html";
        htmlReporter = new ExtentHtmlReporter(reportName);
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName("Functional Report");
        htmlReporter.config().setTheme(Theme.STANDARD);
        reports = new ExtentReports();
        reports.setAnalysisStrategy(AnalysisStrategy.TEST);
        reports.attachReporter(htmlReporter);
        reports.setSystemInfo("Environment", "QA");
        reports.setSystemInfo("Hostname", System.getProperty("user.name"));
        reports.setSystemInfo("OS Version", _os);
        reports.setSystemInfo("OS Architecture", System.getProperty("os.arch"));
        reports.setSystemInfo("Java Version", System.getProperty("java.runtime.version"));
        reports.setSystemInfo("Browser", _browser);
        reports.setSystemInfo("Tester Name", "Automation Tester");

        switch (_browser) {
            case "Chrome": webDriver = new EventFiringWebDriver(StartChrome()); break;
            case "Firefox": webDriver = new EventFiringWebDriver(StartFireFox()); break;
            default: break;
        }
        webDriver.register(new EventReporter());
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterSuite
    public static void tearDown() {
        reports.flush();
        webDriver.close();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
    }

    private static WebDriver StartFireFox() {
        WebDriver webDriver;
        String _driverPath;
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        // replace for simething to get maximized if defined
        if (_os.contains("win")) _driverPath = TestData.getProperty("FiefoxDriver.Windows");
        else if (_os.contains("mac")) _driverPath = TestData.getProperty("FiefoxDriver.MacOS");
        else { _driverPath = TestData.getProperty("FiefoxDriver.Linux"); _headless = true; }
        if (_headless) firefoxOptions.addArguments("-headless");
        webDriver = new FirefoxDriver(firefoxOptions);
        return webDriver;
    }

    private static WebDriver StartChrome() {
        WebDriver webDriver;
        String _driverPath;
        if (_os.contains("win")) _driverPath = TestData.getProperty("ChromeDriver.Windows");
        else if (_os.contains("mac")) _driverPath = TestData.getProperty("ChromeDriver.MacOS");
        else { _driverPath = TestData.getProperty("ChromeDriver.Linux"); _headless = true; }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--no-sandbox");
        if (!_headless) {
            if (_window_position != null) chromeOptions.addArguments("--window-position=" + _window_position);
            if (_window_size != null) chromeOptions.addArguments("--window-size=" + _window_size);
//            if (_start_maximized) chromeOptions.addArguments("start-maximized");
        }

        chromeOptions.setHeadless(_headless);
        System.setProperty("webdriver.chrome.driver", _driverPath);
        webDriver = new ChromeDriver(chromeOptions);
        if (_start_maximized) webDriver.manage().window().maximize();
        return webDriver;
    }

    public static String getScreenshot(WebDriver webDriver, String screenshotName) {
        String scDirName = reportDir + "/" + "screenshots/" + new SimpleDateFormat("yyyyMMddhhmmss").format(testStartTime);
        String scName = scDirName + "/" + screenshotName + ".png";
        TakesScreenshot ts = (TakesScreenshot)webDriver;
        File sc = ts.getScreenshotAs(OutputType.FILE);
        File scDir = new File(scDirName);
        if (!scDir.exists()) scDir.mkdirs();
        File newName = new File(scName);
        sc.renameTo(newName);
        return newName.getAbsolutePath();
    }

    public void justWait(int timeSeconds) {
        try {
            Thread.sleep(timeSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebPage runApplication() {
        webDriver.get(applicationURL);
        return new EnterEmailPage(webDriver);
    }

    public WebPage forceLogout() {
        webDriver.get(applicationURL + TestData.getProperty("Logout_URI"));
        return new EnterEmailPage(webDriver);
    }

    public void deleteAllCookies() {
        webDriver.manage().deleteAllCookies();
    }

}
