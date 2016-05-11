package br.edu.ufcg.embedded.ise.geneguis.consolereader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import junit.framework.Assert;

public class ChromeConsoleReader implements BrowserConsoleReader {

	@Override
	public void readException(WebDriver webDriver, String msg) {
		boolean found = false;
		LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
		
		for (LogEntry logEntry : logEntries) {
			if (logEntry.getMessage() != null && logEntry.getMessage().contains(msg)) {
				found = true;
			}
		}
		
		Assert.assertTrue("(" + msg + ") not found", found);
	}
}
