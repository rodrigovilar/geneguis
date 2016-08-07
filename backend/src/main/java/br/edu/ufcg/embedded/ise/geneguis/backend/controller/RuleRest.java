package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;

public class RuleRest {

	private Long id;
	private Long version;
	private String widgetName;
	private Long widgetVersion;
	private String portName;
	private String type;
	private String entityTypeLocator;
	private String propertyTypeLocator;
	private String tag;
	private PropertyTypeType propertyTypeTypeLocator;
	private String configuration;

	public RuleRest() {
	}

	public RuleRest(String widgetName, String portName) {
		this.widgetName = widgetName;
		this.portName = portName;
	}

	public RuleRest(String widgetName, String portName, String type) {
		this(widgetName, portName);
		this.type = type;
	}

	public RuleRest(String widgetName, String entityTypeLocator, String propertyTypeLocator,
			PropertyTypeType propertyTypeTypeLocator, String portName, String type) {
		this(widgetName, portName, type);
		this.entityTypeLocator = entityTypeLocator;
		this.propertyTypeLocator = propertyTypeLocator;
		this.propertyTypeTypeLocator = propertyTypeTypeLocator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWidgetName() {
		return widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	public Long getWidgetVersion() {
		return widgetVersion;
	}

	public void setWidgetVersion(Long widgetVersion) {
		this.widgetVersion = widgetVersion;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getEntityTypeLocator() {
		return entityTypeLocator;
	}

	public void setEntityTypeLocator(String entityTypeLocator) {
		this.entityTypeLocator = entityTypeLocator;
	}

	public String getPropertyTypeLocator() {
		return propertyTypeLocator;
	}

	public void setPropertyTypeLocator(String propertyTypeLocator) {
		this.propertyTypeLocator = propertyTypeLocator;
	}

	public PropertyTypeType getPropertyTypeTypeLocator() {
		return propertyTypeTypeLocator;
	}

	public void setPropertyTypeTypeLocator(PropertyTypeType propertyTypeTypeLocator) {
		this.propertyTypeTypeLocator = propertyTypeTypeLocator;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
