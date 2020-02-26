package framework;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeUnit;

public class TestSetup {
    protected static WebDriver webDriver;
    protected String applicationURL = TestData.getProperty("App_URL");
    private static boolean _headless, _start_maximized;;
    private static String _browser, _window_position, _window_size, _os;

    @BeforeClass
    @Parameters ({"browser", "headless", "window_position", "window_size", "start_maximized"})
    public static void setUp (@Optional String browser, @Optional String headless, @Optional String window_position, @Optional String window_size, @Optional String start_maximized) {

        // initialize optional veriables
        _headless = (headless != null) && headless.equalsIgnoreCase("true");
        _browser = (browser != null) ? browser : "Chrome";
        _window_position = window_position;
        _window_size = window_size;
        _start_maximized = (start_maximized != null) && start_maximized.equalsIgnoreCase("true");
        _os = System.getProperty("os.name").toLowerCase();

        switch (_browser) {
            case "Chrome": StartChrome(); break;
            case "Firefox": StartFireFox(); break;
            default: break;
        }
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        webDriver.close();
    }

    private static void StartFireFox() {
        String _driverPath;
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        // replace for simething to get maximized if defined
        if (_os.contains("win")) _driverPath = TestData.getProperty("FiefoxDriver.Windows");
        else if (_os.contains("mac")) _driverPath = TestData.getProperty("FiefoxDriver.MacOS");
        else { _driverPath = TestData.getProperty("FiefoxDriver.Linux"); _headless = true; }
        if (_headless) firefoxOptions.addArguments("-headless");
        webDriver = new FirefoxDriver(firefoxOptions);
    }

    private static void StartChrome() {
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
    }

}
