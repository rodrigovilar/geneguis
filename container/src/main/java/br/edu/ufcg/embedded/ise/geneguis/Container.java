package br.edu.ufcg.embedded.ise.geneguis;

import java.util.List;

public class Container {

	private final DomainModel model;
	private final RenderingService renderingService;

	public Container(DomainModel model, RenderingService renderingService) {
		if (model == null) {
			throw new ContainerException("The domain model is mandatory");
		}

		if (renderingService == null) {
			throw new ContainerException("The rendering service is mandatory");
		}

		this.renderingService = renderingService;
		this.model = model;
	}

	public RenderingService getRenderingService() {
		return renderingService;
	}

	public Iterable<EntityType> getEntityTypes() {
		return model.getEntityTypes();
	}

	public EntityType getEntityType(String name) {
		for (EntityType entityType : getEntityTypes()) {
			if (entityType.getName().equals(name)) {
				return entityType;
			}
		}

		return null;
	}

	public List<Entity> getEntities(String resource) {
		return model.getEntities(resource);
	}

	public Entity saveEntity(String name, Entity instance) {
		try {
			return model.saveEntity(name, instance);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public Entity getEntity(String name, Long entityId) {
		return model.getEntity(name, entityId);
	}

	public Entity saveEntity(Long entityId, String name, Entity newEntity) {
		try {
			return model.saveEntity(entityId, name, newEntity);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public boolean deleteEntity(String name, Long entityId) {
		return model.deleteEntity(name, entityId);
	}

	public Widget saveWidget(Widget widget) {
		try {
			return renderingService.saveWidget(widget);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public List<Widget> getAll() {
		try {
			return renderingService.getAll();
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public Widget getWidget(String widgetName) {
		try {
			return renderingService.getWidget(widgetName);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public List<Rule> getAllRulesByVersionGreaterThan(Long version) {
		return renderingService.getAllRulesByVersionGreaterThan(version);
	}

	public List<Rule> getAllRules() {
		return renderingService.getAllRules();
	}

	public Rule saveRule(Rule rule) {
		return renderingService.saveRule(rule);
	}

}
