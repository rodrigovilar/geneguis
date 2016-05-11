package br.edu.ufcg.embedded.ise.geneguis.consolereader;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserReaders {

	private Map<Class, BrowserConsoleReader> readers;

	public BrowserReaders() {
		this.readers = new HashMap<Class, BrowserConsoleReader>();
		this.readers.put(ChromeDriver.class, new ChromeConsoleReader());
		this.readers.put(FirefoxDriver.class, new FirefoxConsoleReader());
	}

	public BrowserConsoleReader getReader(WebDriver webBrowser) {
		return this.readers.get(webBrowser.getClass());
	}
}
