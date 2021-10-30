package com.store.test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.store.pages.ClassplusLoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClassplusAutomationTest {

	protected static String browserDriverPath = null;

	protected static WebDriver driver = null;

	private static String loginURL = "http://wl-ui-5.staging.classplus.co/login";
	protected ClassplusLoginPage loginPage = null;

	protected static Map<String, String> chromeDriverMap = new HashMap<String, String>();

	private static final Logger logger = Logger.getLogger(BaseClassplusAutomationTest.class.getName());

	protected static Properties testDataProp = null;
	protected static Properties expectedAssertionsProp = null;

	private static Map<WEB_DRIVER, WebDriver> webDriverPool = new Hashtable<WEB_DRIVER, WebDriver>();

	protected enum WEB_DRIVER {

		LOGIN_DRIVER, CREATE_BATCH_DRIVER;
	}

	@BeforeSuite
	@Parameters({ "siteURL" })
	public void initTestData(String siteURL) {

		if (siteURL != null) {
			this.loginURL = siteURL;
			chromeDriverMap.put("91", "src/main/resources/drivers/chromedriver91.exe");
			chromeDriverMap.put("90", "src/main/resources/drivers/chromedriver90.exe");
			chromeDriverMap.put("89", "src/main/resources/drivers/chromedriver89.exe");
			chromeDriverMap.put("86", "src/main/resources/drivers/chromedriver86.exe");
			chromeDriverMap.put("87", "src/main/resources/drivers/chromedriver87.exe");
		}

		if (testDataProp == null) {
			FileReader testDataReader = null;
			FileReader assertionsReader = null;

			try {
				testDataReader = new FileReader("src/main/resources/testdata.properties");
				assertionsReader = new FileReader("src/main/resources/expectedassertions.properties");

				testDataProp = new Properties();
				testDataProp.load(testDataReader);

				expectedAssertionsProp = new Properties();
				expectedAssertionsProp.load(assertionsReader);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {

					testDataReader.close();
					assertionsReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("Site URL : " + this.loginURL);
	}

	protected synchronized void quitDriver(WEB_DRIVER webDriver) {
		logger.info("Starting of method quitDriver in BaseClassplusAutomationTest ");

		WebDriver driver = webDriverPool.get(webDriver);
		try {
			if (driver != null) {
				driver.quit();
				driver = null;
				webDriverPool.remove(webDriver);
				logger.debug(webDriver + " Web driver quit successfully in BaseClassplusAutomationTest ");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			driver = null;
		}
		logger.info("Ending of method quitDriver in BaseClassplusAutomationTest");
	}

	@AfterSuite
	protected synchronized void quitAllDrivers() {
		logger.info("Starting of method quitAllDrivers in BaseClassplusAutomationTest ");

		WebDriver driver = null;

		synchronized (webDriverPool) {
			if (!webDriverPool.isEmpty()) {
				for (WEB_DRIVER driverKey : webDriverPool.keySet()) {
					logger.debug("Quitting driver key: " + driverKey);
					synchronized (webDriverPool) {
						driver = webDriverPool.get(driverKey);
						try {

							if (driver != null) {
								Thread.sleep(3000);
								driver.quit();
								driver = null;
								webDriverPool.remove(driverKey);
								Thread.sleep(3000);
								logger.debug("Driver quit successfully in BaseClassplusAutomationTest ");
							}
						} catch (Exception ex) {
							logger.error(ex.getMessage());
							driver = null;
						}
					}

				}

			}
		}
		logger.info("Ending of method quitAllDrivers in BaseClassplusAutomationTest");
	}

	/**
	 * This method is used for get driver
	 * 
	 * @param webDriver
	 * @return
	 */

	protected synchronized WebDriver getWebDriver(String browser, WEB_DRIVER webDriver) {
		logger.info("Starting of method getWebDriver");

		WebDriver driver = webDriverPool.get(webDriver);

		String osPath = System.getProperty("os.name");

		// Use existing driver
		if (driver != null) {
			logger.debug("Using existing web driver " + webDriver);
			return driver;
		}

		if (osPath.contains("Linux")) {

			if (browser.equalsIgnoreCase("Firefox")) {
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions options = new FirefoxOptions();
				options.setHeadless(true);
				options.addArguments("--no-sandbox");
				driver = new FirefoxDriver(options);
			} else if (browser.equalsIgnoreCase("Chrome")) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.setHeadless(true);
				options.addArguments("--no-sandbox");
				driver = new ChromeDriver(options);
			}
		} else if (osPath.contains("Mac OS X")) {
			browserDriverPath = "/usr/bin/safaridriver";

			if (browserDriverPath.contains("safaridriver")) {
				System.setProperty("webdriver.safari.driver", browserDriverPath);
				driver = new SafariDriver();

				logger.debug("Safari driver path " + browserDriverPath);
			}
		} else {

			if (browser.equalsIgnoreCase("Chrome")) {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();

			} else if (browser.equalsIgnoreCase("Firefox")) {
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();

			} else if (browser.equalsIgnoreCase("Chromium")) {
				WebDriverManager.chromiumdriver().setup();
				driver = new EdgeDriver();

			} else if (browser.equalsIgnoreCase("IEDriverServer")) {
				WebDriverManager.iedriver().setup();
				driver = new InternetExplorerDriver();

			}
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);

		logger.info("***************** Driver Successfully Created **************** " + driver.getTitle());

		logger.info("End of method getWebDriver");

		webDriverPool.put(webDriver, driver);

		return driver;
	}

	public void siteLogin(String strOrgCode, String strEmailAddress, WebDriver driver) {
		logger.info("Starting of siteLogin method");

		driver.get(loginURL);

		this.loginPage.loginToClassplusUsingMobileNumber(strOrgCode, strEmailAddress);

		logger.info("Ending of siteLogin method");
	}

	public void goToSite(WebDriver driver) throws Exception {

		logger.debug("Login URL" + loginURL);

		driver.get(loginURL);
	}

}
