package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.EnumType;
import br.edu.ufcg.embedded.ise.geneguis.FieldKind;
import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;
import br.edu.ufcg.embedded.ise.geneguis.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.Port;
import br.edu.ufcg.embedded.ise.geneguis.Rule;
import br.edu.ufcg.embedded.ise.geneguis.Tag;
import br.edu.ufcg.embedded.ise.geneguis.TagRule;
import br.edu.ufcg.embedded.ise.geneguis.TagType;
import br.edu.ufcg.embedded.ise.geneguis.Widget;

public class Converter {

	public static EntityTypeRest toRest(EntityType domain, boolean withDetails) {
		if (domain == null)
			return null;

		EntityTypeRest entityTypeRest = toRest(domain);

		if (withDetails) {
			for (FieldType fieldType : domain.getFieldTypes()) {
				FieldTypeRest rest = null;

				if (fieldType.getKind().equals(FieldKind.Property)) {

					PropertyType propertyType = (PropertyType) fieldType;

					if (PropertyTypeType.enumeration.equals(propertyType.getType())) {
						rest = toRest((EnumType) propertyType);
					} else {
						rest = toRest(propertyType);
					}

				} else {
					rest = toRest((RelationshipType) fieldType);
				}
				
				for (Tag tag : fieldType.getTags()) {
					rest.getTags().add(toRest(tag));
				}

				entityTypeRest.getFieldTypes().add(rest);
			}
			
			for (Tag tag : domain.getTags()) {
				entityTypeRest.getTags().add(toRest(tag));
			}

		}

		return entityTypeRest;
	}

	private static RelationshipTypeRest toRest(RelationshipType relationType) {
		RelationshipTypeRest rest = new RelationshipTypeRest();
		rest.setName(relationType.getName());
		rest.setSourceCardinality(relationType.getSourceCardinality());
		rest.setTargetCardinality(relationType.getTargetCardinality());
		rest.setTargetType(relationType.getTargetType().getName());
		return rest;
	}

	private static PropertyTypeRest toRest(PropertyType propertyType) {
		PropertyTypeRest rest = new PropertyTypeRest();
		rest.setName(propertyType.getName());
		rest.setType(propertyType.getType());
		return rest;
	}

	private static PropertyTypeRest toRest(EnumType enumType) {
		EnumTypeRest rest = new EnumTypeRest();
		rest.setName(enumType.getName());
		rest.setType(PropertyTypeType.enumeration);

		for (String opt : enumType.getEnumValues()) {
			OptionRest optionRest = new OptionRest();
			optionRest.setValue(opt);
			rest.getOptions().add(optionRest);
		}

		return rest;
	}

	private static EntityTypeRest toRest(EntityType domain) {
		EntityTypeRest entityTypeRest = new EntityTypeRest();
		entityTypeRest.setName(domain.getName());
		
		for (Tag tag : domain.getTags()) {
			entityTypeRest.getTags().add(toRest(tag));
		}
		
		return entityTypeRest;
	}

	private static TagRest toRest(Tag tag) {
		TagRest tagRest = new TagRest();
		tagRest.setName(tag.getName());
		tagRest.setValue(tag.getValue());
		return tagRest;
	}

	public static Widget toDomain(WidgetRest rest) {
		if (rest == null)
			return null;

		Widget widget = new Widget();
		widget.setName(rest.getName());
		widget.setVersion(rest.getVersion());

		if (rest.getType() != null) {
			widget.setType(WidgetType.valueOf(rest.getType()));
		}

		for (PortRest portRest : rest.getRequiredPorts()) {
			Port port = toDomain(portRest);
			widget.getRequiredPorts().add(port);
		}

		return widget;
	}

	public static WidgetRest toRest(Widget widget) {
		if (widget == null)
			return null;

		WidgetRest rest = new WidgetRest();
		rest.setName(widget.getName());
		rest.setVersion(widget.getVersion());
		rest.setType(widget.getType().name());

		for (Port port : widget.getRequiredPorts()) {
			PortRest portRest = toRest(port);
			rest.getRequiredPorts().add(portRest);
		}

		return rest;
	}

	public static PortRest toRest(Port port) {
		if (port == null)
			return null;

		PortRest portRest = new PortRest();
		portRest.setName(port.getName());
		portRest.setType(port.getType().name());

		return portRest;
	}

	public static Port toDomain(PortRest rest) {
		if (rest == null)
			return null;

		Port port = new Port();
		port.setName(rest.getName());
		port.setType(WidgetType.valueOf(rest.getType()));
		return port;
	}

	public static RuleRest toRest(Rule rule) {
		RuleRest ruleRest = new RuleRest();
		ruleRest.setId(rule.getId());
		ruleRest.setVersion(rule.getVersion());
		ruleRest.setWidgetName(rule.getWidget().getName());
		ruleRest.setWidgetVersion(rule.getWidget().getVersion());
		ruleRest.setPortName(rule.getPort().getName());
		ruleRest.setType(rule.getType().name());
		ruleRest.setEntityTypeLocator(rule.getEntityTypeLocator());
		ruleRest.setPropertyTypeLocator(rule.getPropertyTypeLocator());
		ruleRest.setPropertyTypeTypeLocator(rule.getPropertyTypeTypeLocator());
		ruleRest.setConfiguration(rule.getConfiguration());
		ruleRest.setTag(rule.getTag());
		return ruleRest;
	}

	public static Rule toDomain(RuleRest ruleRest) {
		Rule rule = new Rule();
		rule.setId(ruleRest.getId());
		rule.setVersion(ruleRest.getVersion());
		rule.setEntityTypeLocator(ruleRest.getEntityTypeLocator());
		rule.setPropertyTypeLocator(ruleRest.getPropertyTypeLocator());
		rule.setPropertyTypeTypeLocator(ruleRest.getPropertyTypeTypeLocator());
		rule.setConfiguration(ruleRest.getConfiguration());
		Widget widget = new Widget();
		widget.setName(ruleRest.getWidgetName());
		widget.setVersion(ruleRest.getWidgetVersion());
		rule.setWidget(widget);
		Port port = new Port();
		port.setName(ruleRest.getPortName());
		rule.setPort(port);
		rule.setType(WidgetType.valueOf(ruleRest.getType()));
		rule.setTag(ruleRest.getTag());
		return rule;
	}

	public static PropertyTypeRest propertyTypeRestFromField(Field field) {
		PropertyTypeRest propertyTypeRest = new PropertyTypeRest();
		propertyTypeRest.setName(field.getName());
		String fieldName = field.getType().getSimpleName();
		propertyTypeRest.setType(Converter.propertyTypeTypeFromString(fieldName));
		return propertyTypeRest;
	}

	public static PropertyTypeType propertyTypeTypeFromString(String typeName) {
		if (typeName.equals("String")) {
			return PropertyTypeType.string;
		}
		if (typeName.equalsIgnoreCase("boolean")) {
			return PropertyTypeType.bool;
		}
		if (typeName.equals("Date")) {
			return PropertyTypeType.date;
		}
		if (typeName.equalsIgnoreCase("double")) {
			return PropertyTypeType.real;
		}
		if (typeName.equals("int") || typeName.equals("Integer")) {
			return PropertyTypeType.integer;
		}
		if (typeName.equalsIgnoreCase("long")) {
			return PropertyTypeType.integer;
		}
		return null;
	}

	public static RelationshipTypeRest relationshipTypeRestFromField(Field field) {
		RelationshipTypeRest relationshipTypeRest = new RelationshipTypeRest();
		relationshipTypeRest.setName(field.getName());

		String typeName = null;

		if (field.getAnnotation(OneToOne.class) != null) {
			Class<?> type = field.getType();
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.One);
			relationshipTypeRest.setTargetCardinality(Cardinality.One);
		} else if (field.getAnnotation(ManyToOne.class) != null) {
			Class<?> type = field.getType();
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.Many);
			relationshipTypeRest.setTargetCardinality(Cardinality.One);
		} else if (field.getAnnotation(OneToMany.class) != null) {
			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			Class<?> type = (Class<?>) genericType.getActualTypeArguments()[0];
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.One);
			relationshipTypeRest.setTargetCardinality(Cardinality.Many);
		} else if (field.getAnnotation(ManyToMany.class) != null) {
			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			Class<?> type = (Class<?>) genericType.getActualTypeArguments()[0];
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.Many);
			relationshipTypeRest.setTargetCardinality(Cardinality.Many);
		}

		EntityTypeRest targetType = new EntityTypeRest();
		targetType.setName(typeName);
		relationshipTypeRest.setTargetType(targetType.getName());

		return relationshipTypeRest;
	}

	public static TagRule toDomain(TagRuleRest rest) {
		TagRule domain = new TagRule();
		domain.setName(rest.getName());
		domain.setEntityLocator(rest.getEntityLocator());
		domain.setFieldLocator(rest.getFieldLocator());
		domain.setId(rest.getId());
		domain.setType(TagType.valueOf(rest.getType()));
		domain.setValue(rest.getValue());
		return domain;
	}

	public static TagRuleRest toRest(TagRule domain) {
		TagRuleRest rest = new TagRuleRest();
		rest.setName(domain.getName());
		rest.setEntityLocator(domain.getEntityLocator());
		rest.setFieldLocator(domain.getFieldLocator());
		rest.setId(domain.getId());
		rest.setType(domain.getType().name());
		rest.setValue(domain.getValue());
		return rest;
	}

}
