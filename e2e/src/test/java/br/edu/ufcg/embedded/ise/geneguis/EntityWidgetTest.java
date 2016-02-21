package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkIds;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTitle;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.click;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
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

	@Test
	public void testEntityTitle() {
		deployEntityType(Customer.class, CustomerRepository.class);
		widget("EntityTypeList", WidgetType.EntityTypeSet,
				new PortRest("entity_type_item", WidgetType.EntityType.name()));
		widget("EntityTypeItem", WidgetType.EntityType, new PortRest("entity_type_page", WidgetType.EntityType.name()));
		widget("EntityTitle", WidgetType.EntityType);
		rule("root", "EntityTypeList");
		rule("entity_type_item", "EntityTypeItem");
		rule("entity_type_page", "EntityTitle");

		openApp();
		clickEntityType(Customer.class);
		checkTitle(Customer.class);
	}

	@Test
	public void testEntityTitleWithThreeWidgets() {
		widget("EntityTitle2", WidgetType.EntityType, new PortRest("entity_list", WidgetType.EntityType.name()));
		widget("EntityUnorderedList", WidgetType.EntityType, new PortRest("entity_item", WidgetType.Entity.name()));
		widget("EntityItem", WidgetType.Entity);

		rule("entity_type_page", "EntityTitle2");
		rule("entity_list", "EntityUnorderedList");
		rule("entity_item", "EntityItem");

		Customer c1 = postEntity(new Customer());
		Customer c2 = postEntity(new Customer());

		openApp();
		clickEntityType(Customer.class);
		checkId("list_" + Customer.class.getSimpleName());

		checkIds("li_" + c1.getId(), "li_" + c2.getId());
	}

	@Test
	public void testChangeWidget() {
		widget("EntityOrderedList", WidgetType.EntityType, new PortRest("entity_item", WidgetType.Entity.name()));
		rule("entity_list", "EntityOrderedList");

		openApp();
		clickEntityType(Customer.class);
		checkId("olist_" + Customer.class.getSimpleName());
	}

	@Test
	public void testPropertyWidget() {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);

		widget("EntityItem2", WidgetType.Entity, new PortRest("property_value", WidgetType.Property.name()));
		widget("SimpleValue", WidgetType.Property);

		rule("entity_item", "EntityItem2");
		rule("property_value", "SimpleValue");

		postEntity(new CustomerDetails("ssn1", "name1", new Date(), 1.0));
		postEntity(new CustomerDetails("ssn2", "name2", new Date(), 2.0));

		openApp();
		clickEntityType(CustomerDetails.class);
	}

	@Test
	public void testListingTable() {
		widget("ListingTable", WidgetType.EntityType, new PortRest("table_head", WidgetType.PropertyType.name()),
				new PortRest("table_line", WidgetType.Entity.name()));
		widget("TableHead", WidgetType.PropertyType);
		widget("TableLine", WidgetType.Entity, new PortRest("line_cell", WidgetType.Property.name()));
		widget("TableCell", WidgetType.Property);

		rule("entity_type_page", "ListingTable");
		rule("table_head", "TableHead");
		rule("table_line", "TableLine");
		rule("line_cell", "TableCell");

		openApp();
		clickEntityType(CustomerDetails.class);
	}

	@Test
	public void testListingTableCreateButton() {
		widget("ListingTableWithCreate", WidgetType.EntityType,
				new PortRest("table_head", WidgetType.PropertyType.name()),
				new PortRest("table_line", WidgetType.Entity.name()),
				new PortRest("creation_form", WidgetType.EntityType.name()));
		widget("CreateForm", WidgetType.EntityType,
				new PortRest("form_line", WidgetType.PropertyType.name()));
		widget("FormLine", WidgetType.PropertyType);

		rule("entity_type_page", "ListingTableWithCreate");
		rule("creation_form", "CreateForm");
		rule("form_line", "FormLine");

		openApp();
		clickEntityType(CustomerDetails.class);
		click("button_new_" + CustomerDetails.class.getSimpleName());
	}

}
