package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.backend.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.backend.Port;
import br.edu.ufcg.embedded.ise.geneguis.backend.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.backend.Rule;
import br.edu.ufcg.embedded.ise.geneguis.backend.Widget;

public class Converter {

	public static EntityTypeRest toRest(EntityType domain) {
		if (domain == null)
			return null;

		EntityTypeRest entityTypeRest = new EntityTypeRest();
		entityTypeRest.setName(domain.getName());

		return entityTypeRest;
	}

	public static Widget toDomain(WidgetRest rest) {
		if (rest == null)
			return null;

		Widget widget = new Widget();
		widget.setName(rest.getName());
		widget.setCode(rest.getCode());
		widget.setId(rest.getId());
		widget.setVersion(rest.getVersion());
		return widget;
	}

	public static WidgetRest toRest(Widget widget) {
		if (widget == null)
			return null;

		WidgetRest rest = new WidgetRest();
		rest.setName(widget.getName());
		rest.setCode(widget.getCode());
		rest.setId(widget.getId());
		rest.setVersion(widget.getVersion());
		return rest;
	}

	public static PortRest toRest(Port port) {
		if (port == null)
			return null;

		PortRest portRest = new PortRest();
		portRest.setName(port.getName());
		portRest.setType(port.getType());

		return portRest;
	}

	public static Port toDomain(PortRest rest) {
		if (rest == null)
			return null;

		Port port = new Port();
		port.setName(rest.getName());
		port.setType(rest.getType());
		return port;
	}

	public static RuleRest toRest(Rule rule) {
		RuleRest ruleRest = new RuleRest();
		ruleRest.setId(rule.getId());
		ruleRest.setVersion(rule.getVersion());
		ruleRest.setWidget(toRest(rule.getWidget()));
		ruleRest.setPort(toRest(rule.getPort()));
		ruleRest.setEntityTypeLocator(rule.getEntityTypeLocator());
		ruleRest.setPropertyTypeLocator(rule.getPropertyTypeLocator());
		ruleRest.setPropertyTypeTypeLocator(rule.getPropertyTypeTypeLocator());
		ruleRest.setConfiguration(rule.getConfiguration());
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
		Widget widget = toDomain(ruleRest.getWidget());
		rule.setWidget(widget);
		Port port = toDomain(ruleRest.getPort());
		rule.setPort(port);
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
		relationshipTypeRest.setTargetType(targetType);

		return relationshipTypeRest;
	}

}
