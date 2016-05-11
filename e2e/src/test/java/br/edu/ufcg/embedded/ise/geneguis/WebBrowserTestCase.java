package br.edu.ufcg.embedded.ise.geneguis;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;

public abstract class WebBrowserTestCase {
	
	static WidgetType EntityTypeSet = WidgetType.EntityTypeSet;
	static WidgetType EntityType = WidgetType.EntityType;
	static WidgetType Entity = WidgetType.Entity;
	static WidgetType PropertyType = WidgetType.PropertyType;
	static WidgetType Property = WidgetType.Property;
	static WidgetType RelationshipType = WidgetType.RelationshipType;
	static WidgetType Relationship = WidgetType.Relationship;
	static WidgetType FieldType = WidgetType.FieldType;
	static WidgetType Field = WidgetType.Field;
	
	
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

	@BeforeClass
	public static void openBrowser() {
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", "/home/anderson/development/chromedriver");
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
	public void testCase() throws Exception {
		deployEntityTypes();
		addWidgets();
		addRules();
		steps();
	}

	abstract void deployEntityTypes() throws Exception;
	abstract void addWidgets();
	abstract void addRules();
	abstract void steps();

}
