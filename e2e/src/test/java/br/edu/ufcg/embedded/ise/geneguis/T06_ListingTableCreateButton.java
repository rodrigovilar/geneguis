package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.click;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;

public class T06_ListingTableCreateButton extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() {
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
		rule("root", "EntityTypeList");
		rule("entity_type_item", "EntityTypeItem");
		rule("entity_type_page", "ListingTableWithCreate");
		rule("table_head", "TableHead");
		rule("table_line", "TableLine");
		rule("line_cell", "TableCell");
		rule("creation_form", "CreateForm");
		rule("form_line", "FormLine");
	}

	@Override
	void steps() {
		postEntity(new CustomerDetails("ssn1", "name1", 1.0));
		postEntity(new CustomerDetails("ssn2", "name2", 2.0));

		openApp();
		clickEntityType(CustomerDetails.class);
		click("button_new_" + CustomerDetails.class.getSimpleName());
	}

}
