package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeRest {

	private String name;
	private List<PropertyTypeRest> propertyTypes = new ArrayList<PropertyTypeRest>();
	private List<RelationshipTypeRest> relationshipTypes = new ArrayList<RelationshipTypeRest>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<PropertyTypeRest> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List<PropertyTypeRest> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public List<RelationshipTypeRest> getRelationshipTypes() {
		return relationshipTypes;
	}

	public void setRelationshipTypes(List<RelationshipTypeRest> relationshipTypes) {
		this.relationshipTypes = relationshipTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((propertyTypes == null) ? 0 : propertyTypes.hashCode());
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
		EntityTypeRest other = (EntityTypeRest) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (propertyTypes == null) {
			if (other.propertyTypes != null)
				return false;
		} else if (!propertyTypes.equals(other.propertyTypes))
			return false;
		return true;
	}

}
