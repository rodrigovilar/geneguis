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

public class T09_CompleteCRUD_Bootstrap extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("TableHead", PropertyType);
		widget("TableCell", Property);
		widget("CreateFormBootstrap", EntityType, new PortRest("form_line", PropertyType.name()));
		widget("FormLineBootstrap", PropertyType);
		widget("ListingTableCrud", EntityType, new PortRest("table_head", PropertyType.name()),
				new PortRest("table_line", Entity.name()),
				new PortRest("creation_form", EntityType.name()));
		widget("TableLineCrud", Entity, new PortRest("line_cell", Property.name()),
				new PortRest("edition_form", Entity.name()));
		widget("EditForm", Entity, new PortRest("edit_form_line", Property.name()));
		widget("EditFormLine", Property);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("table_head", "TableHead", PropertyType);
		rule("line_cell", "TableCell", Property);
		rule("creation_form", "CreateFormBootstrap", EntityType);
		rule("form_line", "FormLineBootstrap", PropertyType);
		rule("entity_type_page", "ListingTableCrud", EntityType);
		rule("table_line", "TableLineCrud", Entity);
		rule("edition_form", "EditForm", Entity);
		rule("edit_form_line", "EditFormLine", Property);
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