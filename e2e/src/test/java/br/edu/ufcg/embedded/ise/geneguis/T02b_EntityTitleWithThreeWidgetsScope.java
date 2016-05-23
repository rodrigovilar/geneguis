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
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Product;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ProductRepository;

public class T02b_EntityTitleWithThreeWidgetsScope extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(Customer.class, CustomerRepository.class);
		deployEntityType(Product.class, ProductRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet,
				new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("EntityTitle2", EntityType, new PortRest("entity_list", EntityType.name()));
		widget("EntityUnorderedList", EntityType, new PortRest("entity_item", Entity.name()));
		widget("EntityItem", Entity);
		widget("EntityItem3", Entity);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "EntityTitle2", EntityType);
		rule("entity_list", "EntityUnorderedList", EntityType);
		rule("entity_item", "Prod*", "EntityItem3", Entity);
		rule("entity_item", "EntityItem", Entity);
	}

	@Override
	void steps() {
		Customer c1 = postEntity(new Customer());
		Customer c2 = postEntity(new Customer());
		Product p1 = postEntity(new Product("111", "name 1", "desc 1", 1.0));
		Product p2 = postEntity(new Product("222", "name 2", "desc 2", 2.0));

		openApp();
		clickEntityType(Customer.class);
		checkId(By.id("list_" + Customer.class.getSimpleName()));
		checkIds("li_" + c1.getId(), "li_" + c2.getId());		
		
		openApp();
		clickEntityType(Product.class);
		checkId(By.id("list_" + Product.class.getSimpleName()));
		checkIds("li_it_" + p1.getId(), "li_it_" + p2.getId());		
	}

}
