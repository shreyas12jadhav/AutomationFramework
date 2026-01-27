package base;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import utils.ReportManager;
import utils.RetryAnalyzer;
import utils.ScreenshotUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseTest {

    // ✅ Thread-safe driver
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected static ExtentReports extent;
    protected ExtentTest test;

    protected static Logger log = LogManager.getLogger(BaseTest.class);

    // ✅ Only way to access driver
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    private boolean isCI() {
        return System.getenv("GITHUB_ACTIONS") != null;
    }


    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public synchronized void setUp(Method method, @Optional("chrome") String browser) {

        // ✅ Initialize report first
        if (extent == null) {
            extent = ReportManager.getReportInstance();
        }

        test = extent.createTest(method.getName());

        //String browser = ConfigReader.get("browser");
       // boolean isCI = System.getenv("GITHUB_ACTIONS") != null;

        WebDriver localDriver;

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            // ✅ Headless for CI
            if (isCI()) {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
            }
            localDriver = new ChromeDriver(options);
        }
        else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            localDriver = new EdgeDriver();
        }
        else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();

            // ✅ Headless for CI
            if (isCI()) {
                options.addArguments("--headless");
            }

            localDriver = new FirefoxDriver(options);
        }
        else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }

        // ✅ CRITICAL: set ThreadLocal BEFORE any usage
        driver.set(localDriver);

        // ✅ NOW it is safe to use getDriver()
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().get(ConfigReader.get("baseUrl"));

        log.info("Starting test: " + method.getName());
        log.info("Launching browser: " + browser);
        
    }
 
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

    	   if (test == null) {
    	        System.out.println("⚠️ Extent test is null, skipping report logging.");
    	    } else {
        String screenshotPath = ScreenshotUtil.takeScreenshot(getDriver(), result.getName());

        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            }
        }
        else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath, "Success Screenshot");
            }
        }
        else if (result.getStatus() == ITestResult.SKIP){
            test.skip("Test Skipped");
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath, "Skipped Screenshot");
            }
        }
        if (!RetryAnalyzer.isRetrying()) {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
        }

        log.info("Finished test: " + result.getName());
    }
    }
            
    
    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }


}
