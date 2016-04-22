package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import br.edu.ufcg.embedded.ise.geneguis.test.Helper;
import br.edu.ufcg.embedded.ise.geneguis.test.PojoDomainModel;

public class ContainerTest {

	@Test(expected = ContainerException.class)
	public void withoutDomainModel() {
		new Container(null);
	}

	@Test
	public void withoutEntityTypes() {
		Container container = new Container(new DomainWithoutEntityTypes());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withOneEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Container container = new Container(domainModel);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, "Product");
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withTwoEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Helper.addEntityType(domainModel, "Customer");
		Container container = new Container(domainModel);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, "Product");
		Helper.assertNextEntityType(entityTypes, "Customer");
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

}

class DomainWithoutEntityTypes implements DomainModel {
	public Iterable<EntityType> getEntityTypes() {
		return new ArrayList<EntityType>();
	}

	public <T> List<T> getEntities(String resource) {
		return null;
	}

	public <T> T saveEntity(String name, T instance) {
		return null;
	}

	public <T> T getEntity(String name, Long entityId) {
		return null;
	}

	public <T> T saveEntity(Long entityId, String name, T newEntity) {
		return null;
	}

	public boolean deleteEntity(String name, Long entityId) {
		return false;
	}

	public void clear() {
	}
}