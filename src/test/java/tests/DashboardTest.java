package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardTest {

    
    @Test (groups = {"regression"})
    public void dummyTest() throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertTrue(true);
        System.out.println("Dashboard test running in: " + Thread.currentThread().getId());
    }

}
