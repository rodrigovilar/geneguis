package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
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

	public List<EntityType> getEntityTypes() {
		List<TagRule> tagRules = renderingService.getAllTagRules();

		List<EntityType> entityTypes = model.getEntityTypes();
		for (EntityType entityType : entityTypes) {
			processTags(entityType, tagRules);
		}
		
		return entityTypes;
	}

	public EntityType getEntityType(String name) {
		for (EntityType entityType : getEntityTypes()) {
			if (entityType.getName().equals(name)) {
				processTags(entityType, renderingService.getAllTagRules());
				return entityType;
			}
		}

		return null;
	}

	private void processTags(EntityType entityType, List<TagRule> tagRules) {
		entityType.setTags(new ArrayList<Tag>());
		
		for (FieldType fieldType : entityType.getFieldTypes()) {
			processTags(entityType, fieldType, tagRules);
		}
		
		for (TagRule tagRule : tagRules) {
			if (TagType.EntityType.equals(tagRule.getType())) {

				if (match(entityType.getName(), tagRule.getEntityLocator())) {
					entityType.tag(tagRule.getName(), tagRule.getValue());
				}
			}
		}
	}

	private void processTags(EntityType entityType, FieldType fieldType, List<TagRule> tagRules) {
		fieldType.setTags(new ArrayList<Tag>());

		for (TagRule tagRule : tagRules) {
			if (TagType.FieldType.equals(tagRule.getType())) {

				if (match(entityType.getName(), tagRule.getEntityLocator()) && match(fieldType.getName(), tagRule.getFieldLocator())) {
					fieldType.tag(tagRule.getName(), tagRule.getValue());
				}
			}
		}
	}

	/**
	 * Source:
	 * http://blog.janjonas.net/2012-03-06/java-test-string-match-wildcard-expression
	 * 
	 * @param text
	 *            Text to test
	 * @param pattern
	 *            (Wildcard) pattern to test
	 * @return True if the text matches the wildcard pattern
	 */
	public static boolean match(String text, String pattern) {
		return text.matches(pattern.replace("?", ".?").replace("*", ".*?"));
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
		return renderingService.saveWidget(widget);
	}

	public List<Widget> getAllWidgets() {
		return renderingService.getAllWidgets();
	}

	public Widget getWidget(String widgetName) {
		return renderingService.getWidget(widgetName);
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

	public TagRule saveTagRule(TagRule tag) {
		return renderingService.saveTagRule(tag);
	}

}
