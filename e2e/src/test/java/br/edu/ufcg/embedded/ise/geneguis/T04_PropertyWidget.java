package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkIds;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;

public class T04_PropertyWidget extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("EntityTitle2", EntityType, new PortRest("entity_list", EntityType.name()));
		widget("EntityOrderedList", EntityType, new PortRest("entity_item", Entity.name()));
		widget("EntityItem2", Entity, new PortRest("property_value", Property.name()));
		widget("SimpleValue", Property);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList");
		rule("entity_type_item", "EntityTypeItem");
		rule("entity_type_page", "EntityTitle2");
		rule("entity_list", "EntityOrderedList");
		rule("entity_item", "EntityItem2");
		rule("property_value", "SimpleValue");
	}

	@Override
	void steps() {
		CustomerDetails c1 = postEntity(new CustomerDetails("ssn1", "name1", 1.0));
		CustomerDetails c2 = postEntity(new CustomerDetails("ssn2", "name2", 2.0));

		openApp();
		clickEntityType(CustomerDetails.class);
		checkId("olist_" + CustomerDetails.class.getSimpleName());
		checkIds("li_" + c1.getId(), "li_" + c2.getId());
	}

}
