package br.edu.ufcg.embedded.ise.geneguis.backend;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.WidgetType;

@Entity
public class Rule {

	@GeneratedValue
	@Id
	private Long id;
	
	private Long version;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Port port;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Widget widget;
	
	private String entityTypeLocator;
	private String propertyTypeLocator;
	private PropertyTypeType propertyTypeTypeLocator;
	private String configuration;
	private WidgetType type;

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

	public Port getPort() {
		return port;
	}

	public void setPort(Port port) {
		this.port = port;
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

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public WidgetType getType() {
		return type;
	}

	public void setType(WidgetType type) {
		this.type = type;
	}
	
}