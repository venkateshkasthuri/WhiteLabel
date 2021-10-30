package com.store.pages;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseClassplusAutomationPage {
	public WebDriver driver = null;

	protected static final Logger logger = Logger.getLogger(BaseClassplusAutomationPage.class.getName());

	public static String TEST_FILE_PATH = null;

	public BaseClassplusAutomationPage(WebDriver driver) {
		this.driver = driver;

		if (TEST_FILE_PATH == null) {

			TEST_FILE_PATH = getTestFilePath();

			logger.debug("In Constructor " + TEST_FILE_PATH);
		}

		PageFactory.initElements(driver, this);
	}

	public String getTestFilePath() {
		String path = "src/test/resources";

		File file = new File(path);
		return file.getAbsolutePath();
	}

	protected void selectDropdown(String id, String value) {
		logger.info("Starting of selectDropdown method");

		Select conditions = new Select(driver.findElement(By.id(id)));
		conditions.selectByValue(value);

		logger.info("Ending of selectDropdown method");
	}

	public void clickOnWebElement(WebElement webelement) {
		logger.info("Starting of clickOnWebElement method");

		JavascriptExecutor jsExec = (JavascriptExecutor) driver;
		jsExec.executeScript("arguments[0].click();", webelement);

		logger.info("Ending of clickOnWebElement method");
	}

	public void scrollDown(int scroll) {
		logger.info("Starting of scrollDown method");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, " + scroll + ")");

		logger.info("Ending of scrollDown method");
	}

	public void refresh() {
		logger.info("Starting of refresh method");

		driver.navigate().refresh();

		logger.info("Ending of refresh method");
	}

	public void impicitWait() {
		logger.info("Starting of impicitWait method");

		driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);

		logger.info("Ending of impicitWait method");
	}

	public void explicitWait(List<WebElement> categoryOptions) {
		logger.info("Staritng of explicitWait method");

		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(ExpectedConditions.visibilityOfAllElements(categoryOptions));

		logger.info("Ending of explicitWait method");
	}

	public void explicitWait(WebElement categoryOptions) {
		logger.info("Staritng of explicitWait method");

		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(ExpectedConditions.visibilityOf(categoryOptions));

		logger.info("Ending of explicitWait method");
	}

	public void pickFromWebElemetList(List<WebElement> webElements, String containsText) {
		logger.info("Staritng of pickFromWebElemetList method");

		for (WebElement webElement : webElements) {
			if (webElement.getText().contains(containsText)) {
				this.clickOnWebElement(webElement);
				break;
			}
		}

		logger.info("Ending of pickFromWebElemetList method");
	}

	public void pickFromWebElemetList(List<WebElement> webElements, List<WebElement> textWebElements, String containsText) {
		logger.info("Staritng of pickFromWebElemetList method");
		
		WebElement webElement = null;
		WebElement textWebElement = null;
		Object[] webElementsArray = webElements.toArray();
		Object[] xPathArray = textWebElements.toArray();
		
		for (int i = 0; i< webElements.size();i++) {
			 webElement = (WebElement) webElementsArray[i];
			 textWebElement = (WebElement) xPathArray[i];
			if (textWebElement.getText().contains(containsText)) {
				this.clickOnWebElement(webElement);
				break;
			}
		}

		logger.info("Ending of pickFromWebElemetList method");
	}
}
