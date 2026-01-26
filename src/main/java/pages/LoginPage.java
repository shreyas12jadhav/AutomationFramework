package pages;

import org.openqa.selenium.By;

import base.BaseTest;
import utils.ConfigReader;
import utils.WaitUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPage {

    private static Logger log = LogManager.getLogger(LoginPage.class);

    private By username = By.id("username");
    private By password = By.id("password");
    private By loginBtn = By.cssSelector("button[type='submit']");
    private By message = By.id("flash");

    public void open() {
        log.info("Opening login page");
        BaseTest.getDriver().manage().deleteAllCookies();
        BaseTest.getDriver().get(ConfigReader.get("baseUrl"));
        WaitUtils.waitForVisibility(username);
    }

    public void login(String user, String pass) {
        log.info("Entering username: " + user);
        log.info("Entering password");

        WaitUtils.type(username, user);
        WaitUtils.type(password, pass);
        WaitUtils.click(loginBtn);
    }

    public String getMessage() {
        return WaitUtils.waitForVisibility(message).getText();
    }
}
