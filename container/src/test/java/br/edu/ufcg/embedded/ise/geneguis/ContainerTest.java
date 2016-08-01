package br.edu.ufcg.embedded.ise.geneguis;

import static br.edu.ufcg.embedded.ise.geneguis.test.Helper.*;

import java.util.Iterator;

import org.junit.Test;

import br.edu.ufcg.embedded.ise.geneguis.test.Helper;
import br.edu.ufcg.embedded.ise.geneguis.test.PojoDomainModel;
import br.edu.ufcg.embedded.ise.geneguis.test.PojoRenderingService;

public class ContainerTest {

	@Test(expected = ContainerException.class)
	public void withoutDomainModel() {
		new Container(null, new PojoRenderingService());
	}

	@Test(expected = ContainerException.class)
	public void withoutRenderingService() {
		new Container(new PojoDomainModel(), null);
	}

	@Test
	public void withoutEntityTypes() {
		Container container = new Container(new PojoDomainModel(), new PojoRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withOneEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Container container = new Container(domainModel, new PojoRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, entityType("Product"));
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withTwoEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Helper.addEntityType(domainModel, "Customer");
		Container container = new Container(domainModel, new PojoRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, entityType("Product"));
		Helper.assertNextEntityType(entityTypes, entityType("Customer"));
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withOneEntityTypeTags() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Container container = new Container(domainModel, new PojoRenderingService());
		Helper.addTagRuleForEntity(container, "Product", "Tag1", "value1");
		Helper.addTagRuleForEntity(container, "Product", "Tag2", null);
		Helper.addTagRuleForField(container, "Product", "*", "Tag3", null);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, entityType("Product", "Tag1;value1", "Tag2"));

		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withOnePropertyType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product").propertyType("desc", PropertyTypeType.string);
		Container container = new Container(domainModel, new PojoRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes,
				entityType("Product", ar(propertyType("desc", PropertyTypeType.string))));
	}

	@Test
	public void withTwoPropertyTypesTags() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product").propertyType("desc", PropertyTypeType.string).propertyType("abc",
				PropertyTypeType.bool);
		Container container = new Container(domainModel, new PojoRenderingService());
		Helper.addTagRuleForEntity(container, "Product", "Tag1", null);
		Helper.addTagRuleForField(container, "Product", "desc", "Tag2", null);
		Helper.addTagRuleForField(container, "Product", "*", "Tag3", null);
		Helper.addTagRuleForField(container, "*", "desc", "Tag4", null);
		Helper.addTagRuleForField(container, "*", "*", "Tag5", null);
		Helper.addTagRuleForField(container, "Product", "desc", "Tag6", "value6");
		Helper.addTagRuleForField(container, "Product", "*", "Tag7", "value7");
		Helper.addTagRuleForField(container, "*", "desc", "Tag8", "value8");
		Helper.addTagRuleForField(container, "*", "*", "Tag9", "value9");

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, 
			entityType("Product", 
				ar(
					propertyType("desc", PropertyTypeType.string, "Tag2", "Tag3", "Tag4", "Tag5", "Tag6;value6", "Tag7;value7", "Tag8;value8", "Tag9;value9"),
					propertyType("abc", PropertyTypeType.bool, "Tag3", "Tag5", "Tag7;value7", "Tag9;value9"))));
	}

}
