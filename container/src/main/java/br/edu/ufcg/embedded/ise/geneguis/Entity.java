package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	private EntityType type;
	private List<Field> fields = new ArrayList<Field>();

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public Long getId() {
		return (Long) getField("id").getValue();
	}

	public void setId(Long id) {
		getField("id").setValue(id);
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	private Field getField(String name) {
		for (Field field : fields) {
			if (field.getType().getName().equals(name)) {
				return field;
			}
		}
		
		return null;
	}

}
