package br.edu.ufcg.embedded.ise.geneguis;

import java.util.List;

public interface DomainModel {

	Iterable<EntityType> getEntityTypes();

	<T> List<T> getEntities(String resource);

	<T> T saveEntity(String name, T instance);

	<T> T getEntity(String name, Long entityId);

	<T> T saveEntity(Long entityId, String name, T newEntity);

	boolean deleteEntity(String name, Long entityId);

}
