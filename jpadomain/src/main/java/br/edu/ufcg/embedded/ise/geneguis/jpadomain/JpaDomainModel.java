package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;

public class JpaDomainModel implements DomainModel {
	
	private Map<EntityType, JpaRepository<?, ?>> repositories = new HashMap<EntityType, JpaRepository<?, ?>>();

	public Iterable<EntityType> getEntityTypes() {
		return repositories.keySet();
	}

	public <T> void deployEntityType(Class<T> clazz, JpaRepository<T, ?> repository) {
		checkJpaEntity(clazz);
		
		String name = clazz.getSimpleName();

		EntityType entityType = new EntityType();
		entityType.setName(name);

		repositories.put(entityType, repository);
	}

	private <T> void checkJpaEntity(Class<T> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		if (entity == null) {
			throw new ContainerException("Deployed classes must be annotated with JPA @Entity");
		}
	}

}
