package com.store.util;

import java.io.File;

import org.testng.log4testng.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	private static ExtentReports extent;
	private static String reportFileName = "test-automation-report.html";
	private static String fileSeperator = System.getProperty("file.separator");
	private static String reportFilepath = System.getProperty("user.dir") + fileSeperator + "test-output";
	private static String reportFileLocation = reportFilepath + fileSeperator + reportFileName;
	private static final Logger logger = Logger.getLogger(ExtentManager.class.getClass());

	public static ExtentReports getInstance() {
		if (extent == null)
			createInstance();
		return extent;
	}

	// Create an extent report instance
	public static ExtentReports createInstance() {
		String fileName = getReportPath(reportFilepath);

		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle(reportFileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(reportFileName);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		// Set environment details
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("AUT", "QA");

		return extent;
	}

	// Create the report path
	private static String getReportPath(String path) {
		if (System.getProperty("os.name").contains("Linux")) {
			reportFilepath = System.getProperty("user.dir") + fileSeperator + "test-output";
		}

		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				logger.debug("Directory: " + path + " is created!");
				return reportFileLocation;
			} else {
				logger.debug("Failed to create directory: " + path);
				return System.getProperty("user.dir");
			}
		} else {
			logger.debug("Directory already exists: " + path);
		}
		logger.debug("File Created Path is " + reportFilepath);
		return reportFileLocation;
	}

}