package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTextByXpath;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.click;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.sendText;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import org.openqa.selenium.By;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;

public class T06_ListingTableCreateButton extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("ListingTableWithCreate", EntityType, new PortRest("table_head", PropertyType.name()),
				new PortRest("table_line", Entity.name()), new PortRest("creation_form", EntityType.name()));
		widget("TableHead", PropertyType);
		widget("TableLine", Entity, new PortRest("line_cell", Property.name()));
		widget("TableCell", Property);
		widget("CreateForm", EntityType, new PortRest("form_line", PropertyType.name()));
		widget("FormLine", PropertyType);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "ListingTableWithCreate", EntityType);
		rule("table_head", "TableHead", PropertyType);
		rule("table_line", "TableLine", Entity);
		rule("line_cell", "TableCell", Property);
		rule("creation_form", "CreateForm", EntityType);
		rule("form_line", "FormLine", PropertyType);
	}

	@Override
	void steps() {
		postEntity(new CustomerDetails("ssn1", "name1", 1.0));
		postEntity(new CustomerDetails("ssn2", "name2", 2.0));

		openApp();
		clickEntityType(CustomerDetails.class);
		click(By.id("button_new_" + CustomerDetails.class.getSimpleName()));
		
		sendText(By.name("create_form_field_ssn"), "ssn3");
		sendText(By.name("create_form_field_name"), "name3");
		sendText(By.name("create_form_field_credit"), "3");	
		
		click(By.xpath(".//form/button"));
		
		checkTextByXpath(".//table[@id = 'table_CustomerDetails']/tbody/tr[3]/td[3]", "name3");

	}

}
