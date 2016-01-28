package br.edu.ufcg.embedded.ise.geneguis.backend;

import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.checkEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.widget;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;

public class EntityWidgetTest {

	static FirefoxDriver driver;

	@BeforeClass
	public static void openBrowser() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void testEntityTitle() {
		Class<?> entityType = Customer.class;
		Class<?> repository = CustomerRepository.class;
		
		deployEntityType(entityType, repository);
		widget("EntityTitle", WidgetType.EntitySet);
		rule("root", "EntityTitle");

		openApp();
		clickEntityType(entityType);
		checkEntityType(entityType);
	}
}
