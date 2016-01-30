package br.edu.ufcg.embedded.ise.geneguis.backend;

import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.checkTitle;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.checkList;
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

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;

public class EntityWidgetTest {

	static FirefoxDriver driver;

	@BeforeClass
	public static void openBrowser() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Ignore
	@Test
	public void testEntityTitle() {
		Class<?> entityType = Customer.class;
		Class<?> repository = CustomerRepository.class;
		
		deployEntityType(entityType, repository);
		widget("EntityTitle", WidgetType.EntitySet);
		rule("root", "EntityTitle");

		openApp();
		clickEntityType(entityType);
		checkTitle(entityType);
	}
	
	@Test
	public void testEntityTitleWithThreeWidgets() {
		Class<?> entityType = Customer.class;
		Class<?> repository = CustomerRepository.class;
		
		deployEntityType(entityType, repository);
		widget("EntityTitle2", WidgetType.EntitySet, new PortRest("list", WidgetType.EntitySet.name()));
		widget("EntityUnorderedList", WidgetType.EntitySet, new PortRest("item", WidgetType.Entity.name()));
		widget("EntityItem", WidgetType.Entity);
		
		rule("root", "EntityTitle2");
		rule("list", "EntityUnorderedList");
		rule("item", "EntityItem");

		openApp();
		clickEntityType(entityType);
		checkList(entityType);
		
		//TODO Add Customer and see their list items
	}

}
