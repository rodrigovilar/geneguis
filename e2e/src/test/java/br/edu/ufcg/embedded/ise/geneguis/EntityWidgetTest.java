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

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;

public class EntityWidgetTest {

	static FirefoxDriver driver;

	@BeforeClass
	public static void openBrowser() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
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

	@Test
	public void testPropertyWidget() {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);

		widget("EntityItem2", WidgetType.Entity, new PortRest("prop", WidgetType.Property.name()));
		widget("SimpleValue", WidgetType.Property);

		rule("item", "EntityItem2");
		rule("prop", "SimpleValue");

		postEntity(new CustomerDetails("ssn1", "name1", new Date(), 1.0));
		postEntity(new CustomerDetails("ssn2", "name2", new Date(), 2.0));

		openApp();
		clickEntityType(CustomerDetails.class);
	}

	@Test
	public void testListingTable() {
		widget("ListingTable", WidgetType.EntitySet, new PortRest("table_head", WidgetType.Property.name()),
				new PortRest("table_line", WidgetType.Entity.name()));
		widget("TableHead", WidgetType.Property);
		widget("TableCell", WidgetType.Property);
		widget("TableLine", WidgetType.Entity, new PortRest("line_cell", WidgetType.Property.name()));
		
		rule("root", "ListingTable");
		rule("table_head", "TableHead");
		rule("table_line", "TableLine");
		rule("line_cell", "TableCell");

		openApp();
		clickEntityType(CustomerDetails.class);
	}

}
