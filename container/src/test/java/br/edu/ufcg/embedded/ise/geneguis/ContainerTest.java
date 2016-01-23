package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class ContainerTest {

	@Test(expected = ContainerException.class)
	public void withoutDomainModel() {
		new Container(null);
	}

	@Test
	public void withoutEntityTypes() {
		Container container = new Container(new DomainWithoutEntityTypes());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withOneEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		addEntityType(domainModel, "Product");
		Container container = new Container(domainModel);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		assertNextEntityType(entityTypes, "Product");
		assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withTwoEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		addEntityType(domainModel, "Product");
		addEntityType(domainModel, "Customer");
		Container container = new Container(domainModel);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		assertNextEntityType(entityTypes, "Product");
		assertNextEntityType(entityTypes, "Customer");
		assertNoMoreEntityTypes(entityTypes);
	}

	private static void addEntityType(PojoDomainModel domainModel, String typeName) {
		domainModel.getPojoEntityTypes().add(new EntityType(typeName));
	}

	private static void assertNoMoreEntityTypes(Iterator<EntityType> entityTypes) {
		Assert.assertFalse(entityTypes.hasNext());
	}

	private static void assertNextEntityType(Iterator<EntityType> entityTypes, String typeName) {
		Assert.assertTrue(entityTypes.hasNext());
		Assert.assertEquals(typeName, entityTypes.next().getName());
	}

}

class DomainWithoutEntityTypes implements DomainModel {
	public Iterable<EntityType> getEntityTypes() {
		return new ArrayList<EntityType>();
	}
}

class PojoDomainModel implements DomainModel {

	private List<EntityType> pojoEntityTypes = new ArrayList<EntityType>();

	public List<EntityType> getPojoEntityTypes() {
		return pojoEntityTypes;
	}

	public Iterable<EntityType> getEntityTypes() {
		return pojoEntityTypes;
	}
}