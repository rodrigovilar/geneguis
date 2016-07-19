package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.List;

public class EnumTypeRest extends PropertyTypeRest {

	private String source;
	private List<OptionRest> options = new ArrayList<OptionRest>();

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<OptionRest> getOptions() {
		return options;
	}

	public void setOptions(List<OptionRest> options) {
		this.options = options;
	}
}
