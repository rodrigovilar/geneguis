package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

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
		widget("TableHead", PropertyType);
		widget("TableCell", Property);

		widget("CreateForm", EntityType, new PortRest("form_line", PropertyType.name()));
		widget("FormLine", PropertyType);

		// O 'ComboBox' abre uma porta chamada 'combo_options'
		widget("ComboBox", PropertyType, new PortRest("combo_options", EnumerationValue.name()));
		// O 'ComboBoxOption' não abre porta
		widget("ComboBoxOption", EnumerationValue);

		widget("ListingTableCrud", EntityType, new PortRest("table_head", PropertyType.name()),
				new PortRest("table_line", Entity.name()), new PortRest("creation_form", EntityType.name()));
		widget("TableLineCrud", Entity, new PortRest("line_cell", Property.name()),
				new PortRest("edition_form", Entity.name()));
		widget("EditForm", Entity, new PortRest("edit_form_line", Property.name()));
		widget("EditFormLine", Property);
	}

	@Override
	void addRules() {
		rule("root", "EntityTypeList", EntityTypeSet);
		rule("entity_type_item", "EntityTypeItem", EntityType);
		rule("table_head", "TableHead", PropertyType);
		rule("line_cell", "TableCell", Property);
		rule("creation_form", "CreateForm", EntityType);

		rule("form_line", "FormLine", PropertyType);
		// Na porta form_line é cadastrado um ComboBox
		rule("form_line", "User", "profile", "ComboBox", PropertyType);
		// Na porta combo_options é cadastrado um ComboBoxOption
		rule("combo_options", "ComboBoxOption", EnumerationValue);

		rule("entity_type_page", "ListingTableCrud", EntityType);
		rule("table_line", "TableLineCrud", Entity);
		rule("edition_form", "EditForm", Entity);
		rule("edit_form_line", "EditFormLine", Property);
	}

	@Override
	void steps() {
		postEntity(new User("name1", "login1", "pws1", ProfileEnum.MANAGER));
		postEntity(new User("name2", "login2", "pws2", ProfileEnum.TECHNICAL_SUPPORT));
		postEntity(new User("name3", "login3", "pws3", ProfileEnum.SIMPLE_USER));

		openApp();
		clickEntityType(User.class);
	}

}
