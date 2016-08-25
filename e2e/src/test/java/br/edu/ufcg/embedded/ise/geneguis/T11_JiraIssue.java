package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.Helper.click;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.clickEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.deployEntityType;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.openApp;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.postEntity;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.rule;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.ruleByTag;
import static br.edu.ufcg.embedded.ise.geneguis.Helper.widget;

import org.openqa.selenium.By;

import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.Component;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.ComponentRepository;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.Issue;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.IssueRepository;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.Project;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.ProjectRepository;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.User;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.UserRepository;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.Version;
import br.edu.ufcg.embedded.ise.geneguis.e2e.issue.VersionRepository;

public class T11_JiraIssue extends WebBrowserTestCase {

	@Override
	void deployEntityTypes() throws Exception {
		deployEntityType(Project.class, ProjectRepository.class);
		deployEntityType(Component.class, ComponentRepository.class);
		deployEntityType(User.class, UserRepository.class);
		deployEntityType(Version.class, VersionRepository.class);
		deployEntityType(Issue.class, IssueRepository.class);
	}

	@Override
	void addWidgets() {
		widget("EntityTypeList", EntityTypeSet, new PortRest("entity_type_item", EntityType.name()));
		widget("EntityTypeItem", EntityType, new PortRest("entity_type_page", EntityType.name()));
		widget("TableHead", PropertyType);
		widget("TableCell", Property);
		widget("CreateForm", EntityType, new PortRest("form_line", PropertyType.name()));
		widget("FormLine", PropertyType);
		widget("ComboBox", PropertyType, new PortRest("combo_options", EnumerationValue.name()));
		widget("ComboBoxOption", EnumerationValue);
		widget("ListingTableCrud", EntityType, new PortRest("table_head", PropertyType.name()),
				new PortRest("table_line", Entity.name()),
				new PortRest("creation_form", EntityType.name()));
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
		
		ruleByTag("form_line", "Enum", "ComboBox", PropertyType);
		rule("combo_options", "ComboBoxOption", EnumerationValue);
		
		rule("entity_type_page", "ListingTableCrud", EntityType);
		rule("table_line", "TableLineCrud", Entity);
		rule("edition_form", "EditForm", Entity);
		rule("edit_form_line", "EditFormLine", Property);
	}

	@Override
	void steps() {
		Project project = new Project("ERP");
		postEntity(project);
		postEntity(project.addComponent("1.", "Frontend"));
		postEntity(project.addComponent("2.", "Backend"));
		postEntity(project.addVersion("0.1"));
		postEntity(project.addVersion("1.0"));
		postEntity(project.addVersion("1.1"));

		openApp();
		clickEntityType(Issue.class);
		click(By.id("button_new_" + Issue.class.getSimpleName()));
	}

}
