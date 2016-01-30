package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkList;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTitle;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;
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
