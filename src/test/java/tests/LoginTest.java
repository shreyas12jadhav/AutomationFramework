package tests;

import base.BaseTest;
import pages.LoginPage;
import utils.ExcelUtil;
import utils.RetryAnalyzer;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage(); // ✅ driver is now ready
    }
    
    @DataProvider(name = "browsers", parallel = true)
    public Object[][] browserProvider() {
        return new Object[][] {
            {"chrome"},
            {"firefox"}
        };
    }
//    @Test(priority = 1)
//    public void testValidLogin() throws InterruptedException {
//        loginPage.open();
//        Thread.sleep(2000);
//        loginPage.login("tomsmith", "SuperSecretPassword!");
//
//        String msg = loginPage.getMessage();
//        System.out.println(msg);
//        Assert.assertTrue(msg.contains("You logged into a secure area"));
//    }
//
//    @Test(priority = 2)
//    public void testInvalidLogin() throws InterruptedException {
//        loginPage.open();
//        Thread.sleep(2000);
//        loginPage.login("tomsmith", "wrongpassword");
//
//        String msg = loginPage.getMessage();
//        System.out.println(msg);
//        Assert.assertTrue(false);
//        //Assert.assertTrue(msg.contains("Your password is invalid!"));
//    }
//
//    @Test(priority = 3)
//    public void testEmptyUsername() throws InterruptedException {
//        loginPage.open();
//        Thread.sleep(2000);
//        loginPage.login("", "SuperSecretPassword!");
//
//        String msg = loginPage.getMessage();
//        System.out.println(msg);
//        Assert.assertTrue(msg.contains("Your username is invalid!"));
//    }
    
//  // #DATA-DRIVEN TESTING (Excel)  
//    
//    @DataProvider(name="loginData", parallel=false)
//    public Object[][] getLoginData() {
//        String path = System.getProperty("user.dir") + "/testdata/LoginData.xlsx";
//        return ExcelUtil.getTestData(path, "Sheet1");
//    }
    
    
    @DataProvider(name = "loginDataWithBrowser", parallel = true)
    public Object[][] getLoginDataWithBrowser() {

        String path = System.getProperty("user.dir") + "/testdata/LoginData.xlsx";
        Object[][] excelData = ExcelUtil.getTestData(path, "Sheet1");

        Object[][] result = new Object[excelData.length * 2][4];

        int index = 0;

        for (int i = 0; i < excelData.length; i++) {
            // chrome
            result[index++] = new Object[] {
                "chrome", excelData[i][0], excelData[i][1], excelData[i][2]
            };
            // firefox
            result[index++] = new Object[] {
                "firefox", excelData[i][0], excelData[i][1], excelData[i][2]
            };
        }

        return result;
    }

    
//    @Test(dataProvider = "loginData")
//    public void testLoginWithMultipleData(String username, String password, String expected) {
//
//        loginPage.open();
//        loginPage.login(username, password);
//
//        String msg = loginPage.getMessage();
//
//        if (expected.equalsIgnoreCase("success")) {
//            Assert.assertTrue(msg.contains("You logged into a secure area"));
//        } 
//        else if (expected.equalsIgnoreCase("invalid")) {
//        	 Assert.assertTrue(false);
//            //Assert.assertTrue(msg.contains("Your password is invalid"));
//        } 
//        else if (expected.equalsIgnoreCase("username_invalid")) {
//            Assert.assertTrue(msg.contains("Your username is invalid"));
//        }
//    }

    // # NEXT FEATURE — TEST GROUPS (SMOKE / REGRESSION)
    
    
    @Test(dataProvider = "loginDataWithBrowser", retryAnalyzer = RetryAnalyzer.class ,groups = {"smoke", "regression"})
    public void testLoginWithMultipleData(String browser, String username, String password, String expected) {

    	System.out.println("LoginTest running in thread: " + Thread.currentThread().getId());
    	
        loginPage.open();
        loginPage.login(username, password);
        
        String msg = loginPage.getMessage();

        if (expected.equalsIgnoreCase("success")) {
            Assert.assertTrue(msg.contains("You logged into a secure area"
            		), "Expected successful login but it failed");
        } 
        else if (expected.equalsIgnoreCase("invalid")) {
        	 //Assert.assertTrue(false);
            Assert.assertTrue(msg.contains("Your password is invalid"), "Expected login failure but it succeeded");
        } 
        else if (expected.equalsIgnoreCase("username_invalid")) {
            Assert.assertTrue(msg.contains("Your username is invalid"), "Expected login failure but it succeeded");
        }
    }
    

    
    
}
