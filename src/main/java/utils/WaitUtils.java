package utils;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import base.BaseTest;

public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = 10;

    private static WebDriverWait getWait() {
        return new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ✅ Stale-safe click
    public static void click(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                waitForClickable(locator).click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        // final attempt (let it throw real error)
        waitForClickable(locator).click();
    }

    // ✅ Stale-safe type
    public static void type(By locator, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement el = waitForVisibility(locator);
                el.clear();
                el.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    public static void waitForTitleContains(String titlePart) {
        getWait().until(ExpectedConditions.titleContains(titlePart));
    }
}
