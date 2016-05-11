package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTextByXpath;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Product;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ProductRepository;

public class T05_ListingTable extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(CustomerDetails.class, CustomerDetailsRepository.class);
		deployEntityType(Product.class, ProductRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("ListingTable", EntityType, new PortRest("table_head", PropertyType.name()),
				new PortRest("table_line", Entity.name()));
		widget("TableHead", PropertyType);
		widget("TableLine", Entity, new PortRest("line_cell", Property.name()));
		widget("TableCell", Property);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "ListingTable", EntityType);
		rule("table_head", "TableHead", PropertyType);
		rule("table_line", "TableLine", Entity);
		rule("line_cell", "TableCell", Property);
	}

	@Override
	void steps() {
		postEntity(new CustomerDetails("ssn1", "name1", 1.0));
		postEntity(new CustomerDetails("ssn2", "name2", 2.0));
		postEntity(new Product("00123451", "Product 1", "Description of product 1", 1.0));
		postEntity(new Product("00123499", "Product 2", "Description of product 2", 2.0));
		postEntity(new Product("00123777", "Product 3", "Description of product 3", 3.5));

		openApp();
		clickEntityType(Product.class);
		
		checkTextByXpath(".//table[@id = 'table_Product']/tbody/tr[1]/td[4]", "Description of product 1");
	}

}
