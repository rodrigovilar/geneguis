package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.util.Iterator;

import org.junit.Test;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.examples.Item;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.examples.Product;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.examples.Supplier;
import br.edu.ufcg.embedded.ise.geneguis.test.Helper;

public class JpaDomainModelTest {

	@Test
	public void withoutEntitites() {
		JpaDomainModel domainModel = new JpaDomainModel();
		JpaRenderingService renderingService = new JpaRenderingService();
		Container container = new Container(domainModel, renderingService);
		
		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test(expected=ContainerException.class)
	public void entityTypeWithoutJpaEntity() {
		JpaDomainModel domainModel = new JpaDomainModel();
		domainModel.deployEntityType(Supplier.class, new RepositoryAdapter<Supplier,Long>());
	}

	@Test
	public void withOneEntityType() {
		JpaDomainModel domainModel = new JpaDomainModel();
		domainModel.deployEntityType(Item.class, new RepositoryAdapter<Item,Long>());
		JpaRenderingService renderingService = new JpaRenderingService();
		Container container = new Container(domainModel, renderingService);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, "Item");
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withTwoEntityTypes() {
		JpaDomainModel domainModel = new JpaDomainModel();
		domainModel.deployEntityType(Item.class, new RepositoryAdapter<Item,Long>());
		domainModel.deployEntityType(Product.class, new RepositoryAdapter<Product,Long>());
		JpaRenderingService renderingService = new JpaRenderingService();
		Container container = new Container(domainModel, renderingService);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, "Item");
		Helper.assertNextEntityType(entityTypes, "Product");
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

}
