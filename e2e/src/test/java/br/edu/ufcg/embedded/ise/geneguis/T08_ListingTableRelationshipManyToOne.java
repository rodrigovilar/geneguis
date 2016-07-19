package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Client;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ClientRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Dependent;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.DependentRepository;

public class T08_ListingTableRelationshipManyToOne extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(Dependent.class, DependentRepository.class);	
		deployEntityType(Client.class, ClientRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("FieldListingTable", EntityType, new PortRest("table_head", FieldType.name()),
				new PortRest("table_line", Entity.name()));
		widget("FieldTableHead", FieldType);
		widget("FieldTableLine", Entity, new PortRest("line_cell", Field.name()));
		widget("TableCell", Property);
		widget("NamedRelation", Relationship);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "FieldListingTable", EntityType);
		rule("table_head", "FieldTableHead", FieldType);
		rule("table_line", "FieldTableLine", Entity);
		rule("line_cell", "NamedRelation", Relationship);
		rule("line_cell", "TableCell", Property);
	}

	@Override
	void steps() {
		Client c1 = new Client("client 1");
		postEntity(c1);
		Client c2 = new Client("client 2");
		postEntity(c2);
		postEntity(c1.addDependent("dependent 1", 10));
		postEntity(c1.addDependent("dependent 2", 11));
		postEntity(c2.addDependent("dependent 3", 12));
		postEntity(c2.addDependent("dependent 4", 13));
		postEntity(c2.addDependent("dependent 5", 14));

		openApp();
		clickEntityType(Dependent.class);
	}

}
