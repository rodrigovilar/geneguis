package br.edu.ufcg.embedded.ise.geneguis;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;

public abstract class WebBrowserTestCase {
	
	static WidgetType EntityTypeSet = WidgetType.EntityTypeSet;
	static WidgetType EntityType = WidgetType.EntityType;
	static WidgetType Entity = WidgetType.Entity;
	static WidgetType PropertyType = WidgetType.PropertyType;
	static WidgetType Property = WidgetType.Property;
	
	
	/**
	 * To run Selenium WebDriver test cases in Chrome
	 * you need to download the executable driver from:
	 * https://sites.google.com/a/chromium.org/chromedriver/downloads
	 * 
	 * Use the follow code: 
	 * 
	 * System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
	 * WebDriver driver = new ChromeDriver(); 
	 */
	
	static WebDriver driver;
//	static FirefoxDriver driver;

	@BeforeClass
	public static void openBrowser() {
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", "/home/anderson/Downloads/chromedriver");
			driver = new ChromeDriver();
//			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
	}
	
	@Before
	public void clear() {
		driver.get(Helper.SERVER_URL + "test/reset");
	}

	@Test
	public void testCase() {
		deployEntityTypes();
		addWidgets();
		addRules();
		steps();
	}

	abstract void deployEntityTypes();
	abstract void addWidgets();
	abstract void addRules();
	abstract void steps();

}
