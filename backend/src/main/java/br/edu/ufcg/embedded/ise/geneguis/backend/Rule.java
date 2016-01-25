package br.edu.ufcg.embedded.ise.geneguis.backend;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((entityTypeLocator == null) ? 0 : entityTypeLocator.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((propertyTypeLocator == null) ? 0 : propertyTypeLocator.hashCode());
		result = prime * result + ((propertyTypeTypeLocator == null) ? 0 : propertyTypeTypeLocator.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((widget == null) ? 0 : widget.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (entityTypeLocator == null) {
			if (other.entityTypeLocator != null)
				return false;
		} else if (!entityTypeLocator.equals(other.entityTypeLocator))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (propertyTypeLocator == null) {
			if (other.propertyTypeLocator != null)
				return false;
		} else if (!propertyTypeLocator.equals(other.propertyTypeLocator))
			return false;
		if (propertyTypeTypeLocator != other.propertyTypeTypeLocator)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (widget == null) {
			if (other.widget != null)
				return false;
		} else if (!widget.equals(other.widget))
			return false;
		return true;
	}
	
	
}