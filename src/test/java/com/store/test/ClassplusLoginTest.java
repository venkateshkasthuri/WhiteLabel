package com.store.test;

import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.store.pages.ClassplusLoginPage;

public class ClassplusLoginTest extends BaseClassplusAutomationTest {

	private WebDriver driver = null;

	private static final Logger logger = Logger.getLogger(ClassplusLoginTest.class.getName());

	@BeforeClass
	@Parameters({ "browser" })
	public void initClassplusSiteLogin(String browser) throws Exception {
		logger.info("Starting of initClassplusSiteLogin methond");

		this.driver = this.getWebDriver(browser, WEB_DRIVER.LOGIN_DRIVER);
		this.loginPage = new ClassplusLoginPage(driver);

		this.goToSite(this.driver);

		logger.info("Ending of initClassplusSiteLogin method");
	}

	@AfterClass
	public void quitDriver() {

		try {

			if (driver != null) {

				this.quitDriver(WEB_DRIVER.LOGIN_DRIVER);
				logger.debug("Driver quit successfully");

			}
		} catch (Exception ex) {

			logger.error(ex.getMessage());

		}
	}

	@Test(priority = 1, description = "site login")
	@Parameters({ "orgCode", "mobileNumber" })
	public void loginToSite(String orgCode, String mobileNumber) {
		logger.info("Starting of LoginToSite method");

		this.siteLogin(orgCode, mobileNumber, this.driver);
		
		String getOTPSentMessageText = this.loginPage.getOTPSentMessageText();
		Assert.assertEquals(getOTPSentMessageText, expectedAssertionsProp.getProperty("otp.sent.successfully.text"));
		
		String getOTPHeadingText = this.loginPage.getOTPHeadingText();
		Assert.assertEquals(getOTPHeadingText, expectedAssertionsProp.getProperty("otp.heading.text"));

		logger.info("Ending of LoginTo Site method");
	}

	@Test(priority = 2, description = "verify otp")
	public void verifyOTP() {
		logger.info("Starting of verify OTP method");

		
		this.loginPage.setOTP("1234");
		this.loginPage.clickVerifyOTP();
	
		String otpVerifiedMessageText = this.loginPage.getOTPVerifiedMessageText();
		Assert.assertEquals(otpVerifiedMessageText, expectedAssertionsProp.getProperty("otp.verified.successfully.text"));
		
		String dashboardText = this.loginPage.getDashBoardText();
		Assert.assertEquals(dashboardText, expectedAssertionsProp.getProperty("dashboard.text"));

		logger.info("Ending of verifyOTP method");
	}

}
