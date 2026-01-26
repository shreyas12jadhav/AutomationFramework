package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	private int retryCount = 0;
	private static final int maxRetryCount =1; // retry 2 times
	
	 private static ThreadLocal<Boolean> retrying = ThreadLocal.withInitial(() -> false);
	
	public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;
            retrying.set(true);
            return true;  // 🔁 rerun test
        }
        retrying.set(false);
        return false;     // ❌ stop retrying
    }

	
	public static boolean isRetrying() {
        return retrying.get();
    }
}
