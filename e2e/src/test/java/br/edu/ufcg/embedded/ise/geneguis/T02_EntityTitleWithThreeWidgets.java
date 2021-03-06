package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkIds;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import org.openqa.selenium.By;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;

public class T02_EntityTitleWithThreeWidgets extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(Customer.class, CustomerRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet,
				new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("EntityTitle2", EntityType, new PortRest("entity_list", EntityType.name()));
		widget("EntityUnorderedList", EntityType, new PortRest("entity_item", Entity.name()));
		widget("EntityItem", Entity);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "EntityTitle2", EntityType);
		rule("entity_list", "EntityUnorderedList", EntityType);
		rule("entity_item", "EntityItem", Entity);
	}

	@Override
	void steps() {
		Entity c1 = postEntity(new Customer());
		Entity c2 = postEntity(new Customer());

		openApp();
		clickEntityType(Customer.class);
		checkId(By.id("list_" + Customer.class.getSimpleName()));
		checkIds("li_" + c1.getId(), "li_" + c2.getId());		
	}

}
