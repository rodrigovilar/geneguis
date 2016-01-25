package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;

public class RuleRest {

	private long id;
	private long version;
	private WidgetRest widget;
	private PortRest port;
	private String entityTypeLocator;
	private String propertyTypeLocator;
	private PropertyTypeType propertyTypeTypeLocator;
	private String configuration;

	public long getId() {
		return id;
	}

	
	public PortRest getPort() {
		return port;
	}


	public void setPort(PortRest port) {
		this.port = port;
	}


	public void setId(long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public WidgetRest getWidget() {
		return widget;
	}


	public void setWidget(WidgetRest widget) {
		this.widget = widget;
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

	public void setPropertyTypeTypeLocator(
			PropertyTypeType propertyTypeTypeLocator) {
		this.propertyTypeTypeLocator = propertyTypeTypeLocator;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
}
