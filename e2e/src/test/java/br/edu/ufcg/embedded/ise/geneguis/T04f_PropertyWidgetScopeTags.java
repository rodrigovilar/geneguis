package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTextById;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.ruleByTag;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.tagRule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import org.openqa.selenium.By;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Product;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ProductRepository;

public class T04f_PropertyWidgetScopeTags extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);
		deployEntityType(Product.class, ProductRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("EntityTitle2", EntityType, new PortRest("entity_list", EntityType.name()));
		widget("EntityOrderedList", EntityType, new PortRest("entity_item", Entity.name()));
		widget("EntityItem2", Entity, new PortRest("property_value", Property.name()));
		widget("SimpleValue", Property);
		widget("SimpleValue2", Property);
		widget("SimpleValue3", Property);
		widget("SimpleValue4", Property);
		widget("SimpleValue5", Property);
	}

	@Override
	void addRules() {
		tagRule("Mask", TagType.FieldType, "000-00-0000", "CustomerDetails", "ssn");

		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "EntityTitle2", EntityType);
		rule("entity_list", "EntityOrderedList", EntityType);
		rule("entity_item", "EntityItem2", Entity);
		rule("property_value", "SimpleValue", Property);
		ruleByTag("property_value", "Mask", "SimpleValue5", Property);

		rule("property_value", "Product", "description", "SimpleValue3", Property);
		rule("property_value", "*", "na*", "SimpleValue4", Property);
	}

	@Override
	void steps() {
		Entity c1 = postEntity(new CustomerDetails("ssn1", "name1", 1.0));
		Entity c2 = postEntity(new CustomerDetails("ssn2", "name2", 2.0));
		Entity p1 = postEntity(new Product("111", "name 1", "desc 1", 1.0));
		Entity p2 = postEntity(new Product("222", "name 2", "desc 2", 2.0));

		openApp();
		clickEntityType(CustomerDetails.class);
		checkId(By.id("olist_CustomerDetails"));

		checkTextById("li_" + c1.getId(), "Mask(ssn1) [name1] 1;");
		checkTextById("li_" + c2.getId(), "Mask(ssn2) [name2] 2;");

		openApp();
		clickEntityType(Product.class);
		checkId(By.id("olist_Product"));
		checkTextById("li_" + p1.getId(), "111; [name 1] desc 1/1;");
		checkTextById("li_" + p2.getId(), "222; [name 2] desc 2/2;");
	}

}
