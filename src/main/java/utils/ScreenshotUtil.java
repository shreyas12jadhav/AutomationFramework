package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.google.common.io.Files;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            if (driver == null) {
                System.out.println("❌ Driver is null. Screenshot not captured.");
                return null;
            }

            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date());

            String screenshotDir = System.getProperty("user.dir") + File.separator + "screenshots";
            new File(screenshotDir).mkdirs(); // ensure folder exists

            String screenshotName = testName + "_" + timestamp + ".png";
            String fullPath = screenshotDir + File.separator + screenshotName;

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);           
            File dest = new File(fullPath);
            Files.copy(src, dest);
            
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(testName, "image/png", new ByteArrayInputStream(screenshot), ".png");

            System.out.println("✅ Screenshot saved at: " + fullPath);
            return fullPath;

        } catch (Exception e) {
            System.out.println("❌ Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
