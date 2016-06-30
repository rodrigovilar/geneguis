package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkId;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.checkTextById;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import org.openqa.selenium.By;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ProfileEnum;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.User;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.UserRepository;

public class T04e_PropertyMultipleValuesWidget extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(User.class, UserRepository.class);
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
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("entity_type_page", "EntityTitle2", EntityType);
		rule("entity_list", "EntityOrderedList", EntityType);
		rule("entity_item", "EntityItem2", Entity);
		rule("property_value", "SimpleValue", Property);
	}

	@Override
	void steps() {
		User u1 = postEntity(new User("user1", "login1", "psw1", ProfileEnum.MANAGER));
		System.out.println(u1);
		openApp();
		clickEntityType(User.class);
		checkId(By.id("olist_User"));

		checkTextById("li_" + u1.getId(), "user1; login1; psw1; " + ProfileEnum.MANAGER + ";");
	}
}
