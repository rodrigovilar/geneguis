package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

public class EntityType {

	private String name;
	private List<PropertyType> propertyTypes = new ArrayList<PropertyType>();
	private List<RelationshipType> relationshipTypes = new ArrayList<RelationshipType>();

	
	public EntityType() {}

	public EntityType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<PropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List<PropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public List<RelationshipType> getRelationshipTypes() {
		return relationshipTypes;
	}

	public void setRelationshipTypes(List<RelationshipType> relationshipTypes) {
		this.relationshipTypes = relationshipTypes;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		EntityType other = (EntityType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
