package br.edu.ufcg.embedded.ise.geneguis.consolereader;

import junit.framework.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

public class ChromeConsoleReader implements BrowserConsoleReader {

	@Override
	public void readException(WebDriver webDriver, String url, String... msgs) {
		LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
		for (int i = 0; i < msgs.length; i++) {
//			Assert.assertEquals(msgs[i], logEntries.getAll().get(i).getLevel());
			Assert.assertEquals(msgs[i], logEntries.getAll().get(i).getMessage());
		}
	}
}
