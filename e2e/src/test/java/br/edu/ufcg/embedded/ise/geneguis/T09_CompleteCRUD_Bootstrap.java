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
		widget("TableHeadMaterial", PropertyType);
		widget("TableCell", Property);
		widget("CreateFormMaterial", EntityType, new PortRest("form_line", PropertyType.name()));
		widget("FormLineMaterial", PropertyType);
		widget("ListingTableCrudMaterial", EntityType, new PortRest("table_head", PropertyType.name()),
				new PortRest("table_line", Entity.name()),
				new PortRest("creation_form", EntityType.name()));
		widget("TableLineCrudMaterial", Entity, new PortRest("line_cell", Property.name()),
				new PortRest("edition_form", Entity.name()));
		widget("EditFormMaterial", Entity, new PortRest("edit_form_line", Property.name()));
		widget("EditFormLineMaterial", Property);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("table_head", "TableHeadMaterial", PropertyType);
		rule("line_cell", "TableCell", Property);
		rule("creation_form", "CreateFormMaterial", EntityType);
		rule("form_line", "FormLineMaterial", PropertyType);
		rule("entity_type_page", "ListingTableCrudMaterial", EntityType);
		rule("table_line", "TableLineCrudMaterial", Entity);
		rule("edition_form", "EditFormMaterial", Entity);
		rule("edit_form_line", "EditFormLineMaterial", Property);
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
