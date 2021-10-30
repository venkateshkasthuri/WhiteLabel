package com.store.util;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {
	private final Logger logger = Logger.getLogger(TestListener.class.getClass());

	public void onStart(ITestContext context) {
		logger.info("========================================================================");
		logger.debug("*** Test Suite " + context.getName() + " started ***");
		logger.info("========================================================================");
	}

	public void onFinish(ITestContext context) {
		logger.info("========================================================================");
		logger.debug(("*** Test Suite " + context.getName() + " ending ***"));
		logger.info("========================================================================");
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();
	}

	public void onTestStart(ITestResult result) {
		logger.info("========================================================================");
		logger.debug(("*** Running test method " + result.getMethod().getMethodName() + "***"));
		logger.info("========================================================================");
		ExtentTestManager.startTest(result.getMethod().getMethodName());
	}

	public void onTestSuccess(ITestResult result) {
		logger.info("========================================================================");
		logger.debug("*** Executed " + result.getMethod().getMethodName() + " test successfully***");
		logger.info("========================================================================");
		ExtentTestManager.getTest().log(Status.PASS, "Test passed");
	}

	public void onTestSkipped(ITestResult result) {
		logger.info("========================================================================");
		logger.debug("*** Test " + result.getMethod().getMethodName() + " skipped***");
		logger.info("========================================================================");
		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		logger.info("========================================================================");
		logger.debug("*** Test failed but within percentage % " + result.getMethod().getMethodName());
		logger.info("========================================================================");
	}

}
