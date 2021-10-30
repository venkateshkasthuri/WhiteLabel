package com.store.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class ScreenshotListener extends TestListenerAdapter {

	protected WebDriver driver = null;
	protected static String chromeDriver = "src/main/resources/drivers/chromedriver86.exe";

	@Override
	public void onTestFailure(ITestResult result) {
		initializeDriver();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String methodName = result.getName();
		if (!result.isSuccess()) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
						+ "/test-output/screenshot-reports";
				File destFile = new File((String) reportDirectory + "/failure_screenshots/" + methodName + "_"
						+ formater.format(calendar.getTime()) + ".png");
				FileUtils.copyFile(scrFile, destFile);
				Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
						+ "' height='100' width='100'/> </a>");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (driver != null) {
					driver.close();
				}

			}
		}
	}

	private void initializeDriver() {

		System.setProperty("webdriver.chrome.driver", chromeDriver);
		driver = new ChromeDriver();
		/*
		 * driver.manage().window().maximize(); driver.manage().deleteAllCookies();
		 * driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		 * driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		 */
	}
}