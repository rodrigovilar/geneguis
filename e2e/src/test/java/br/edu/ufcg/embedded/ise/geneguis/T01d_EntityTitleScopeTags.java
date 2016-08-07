package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.ruleByTag;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.tagRule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import org.openqa.selenium.By;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Product;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ProductRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.User;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.UserRepository;

public class T01d_EntityTitleScopeTags extends WebBrowserTestCase {

	void deployEntityTypes() throws Exception {
		deployEntityType(Customer.class, CustomerRepository.class);
		deployEntityType(Product.class, ProductRepository.class);
		deployEntityType(User.class, UserRepository.class);
	}
	
	void addWidgets() {
		widget("EntityTypeList", WidgetType.EntityTypeSet,
				new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("EntityTitle", EntityType);
		widget("EntityTitle3", EntityType);
		widget("EntityTitle4", EntityType);
	}

	void addRules() {
		tagRule("Vip", TagType.EntityType, "V.I.P.", "User", null);
		
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "Product", "EntityTitle3", EntityType);
		ruleByTag("entity_type_page", "Vip", "EntityTitle4", EntityType);
		rule("entity_type_page", "EntityTitle", EntityType);		
	}

	void steps() {
		openApp();
		clickEntityType(Customer.class);
		checkId(By.id("title_Customer"));

		openApp();
		clickEntityType(Product.class);
		checkId(By.id("title_it_Product"));
		
		openApp();
		clickEntityType(User.class);
		checkId(By.id("title_bo_User"));
	}
	
}
