package Utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int maxRetry = 2;

    public boolean retry(ITestResult result) {
        return count++ < maxRetry;
    }
}