package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTitle;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;

public class T01_EntityTitle extends WebBrowserTestCase {

	void deployEntityTypes() {
		deployEntityType(Customer.class, CustomerRepository.class);
	}
	
	void addWidgets() {
		widget("EntityTypeList", WidgetType.EntityTypeSet,
				new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("EntityTitle", EntityType);
	}

	void addRules() {
		rule("root", "EntityTypeList");
		rule("entity_type_item", "EntityTypeItem");
		rule("entity_type_page", "EntityTitle");
	}

	void steps() {
		openApp();
		clickEntityType(Customer.class);
		checkTitle(Customer.class);
	}
	
}
