package br.edu.ufcg.embedded.ise.geneguis;

import java.io.Serializable;
import java.util.List;

public interface DomainModel {

	List<EntityType> getEntityTypes();

	<T> List<Entity> getEntities(String resource);

	<T> Entity saveEntity(String name, Entity instance) throws Exception;

	<T,K extends Serializable> Entity getEntity(String name, K entityId);

	<T,K extends Serializable> Entity saveEntity(K entityId, String name, Entity newEntity) throws Exception;

	<K extends Serializable> boolean deleteEntity(String name, K entityId);

	void clear();
}
