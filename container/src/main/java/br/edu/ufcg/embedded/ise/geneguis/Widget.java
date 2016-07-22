package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.WidgetType;

public class Widget {

	private String name;
	
	private Long version;
	
	private String code;
	
	private WidgetType type;
	
	private List<Port> requiredPorts = new ArrayList<Port>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WidgetType getType() {
		return type;
	}

	public void setType(WidgetType type) {
		this.type = type;
	}

	public List<Port> getRequiredPorts() {
		return requiredPorts;
	}

	public void setRequiredPorts(List<Port> requiredPorts) {
		this.requiredPorts = requiredPorts;
	}


}
