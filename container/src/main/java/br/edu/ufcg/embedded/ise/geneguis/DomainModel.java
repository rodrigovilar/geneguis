package br.edu.ufcg.embedded.ise.geneguis;

import java.util.List;

public interface DomainModel {

	Iterable<EntityType> getEntityTypes();

	<T> List<Entity> getEntities(String resource);

	<T> Entity saveEntity(String name, Entity instance) throws Exception;

	<T> Entity getEntity(String name, Long entityId);

	<T> Entity saveEntity(Long entityId, String name, Entity newEntity) throws Exception;

	boolean deleteEntity(String name, Long entityId);

	void clear();
}
