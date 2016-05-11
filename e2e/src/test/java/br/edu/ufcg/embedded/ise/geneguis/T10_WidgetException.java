package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.exception;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;

public class T10_WidgetException extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(Customer.class, CustomerRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", WidgetType.EntityTypeSet, new PortRest(
				"entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page",
				EntityType.name()));
		widget("EntityTitleWithUnknownFilter", EntityType);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "EntityTitleWithUnknownFilter", EntityType);
	}

	@Override
	void steps() {
		openApp();
		clickEntityType(Customer.class);
		exception(driver, "Error: filter not found: unknownFilter");
	}

}
