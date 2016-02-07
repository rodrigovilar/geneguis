package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkIds;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTitle;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Ignore;
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

	@Ignore
	@Test
	public void testEntityTitle() {
		deployEntityType(Customer.class, CustomerRepository.class);
		widget("EntityTitle", WidgetType.EntitySet);
		rule("root", "EntityTitle");

		openApp();
		clickEntityType(Customer.class);
		checkTitle(Customer.class);
	}
	
	@Test
	public void testEntityTitleWithThreeWidgets() {
		deployEntityType(Customer.class, CustomerRepository.class);
		widget("EntityTitle2", WidgetType.EntitySet, new PortRest("list", WidgetType.EntitySet.name()));
		widget("EntityUnorderedList", WidgetType.EntitySet, new PortRest("item", WidgetType.Entity.name()));
		widget("EntityItem", WidgetType.Entity);
		
		rule("root", "EntityTitle2");
		rule("list", "EntityUnorderedList");
		rule("item", "EntityItem");

		Customer c1 = postEntity(new Customer());
		Customer c2 = postEntity(new Customer());
		
		openApp();		
		clickEntityType(Customer.class);
		checkId("list_" + Customer.class.getSimpleName());
		
		checkIds("li_" + c1.getId(), "li_" + c2.getId());
	}
	
	@Test
	public void testChangeWidget() {
		widget("EntityOrderedList", WidgetType.EntitySet, new PortRest("item", WidgetType.Entity.name()));
		rule("list", "EntityOrderedList");

		openApp();		
		clickEntityType(Customer.class);
		checkId("olist_" + Customer.class.getSimpleName());
	}

}
