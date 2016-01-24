package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.edu.ufcg.embedded.ise.geneguis.backend.Rule;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.RuleService;

@Controller
@RequestMapping(value = "/rules")
public class RulesController {

	@Autowired
	private RuleService service;

	public void setRuleService(RuleService rulesContainer) {
		this.service = rulesContainer;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<RuleRest>> getAll(@RequestParam(value = "version", defaultValue = "0") Long version) {
		List<Rule> rules = null;
		if (version != 0) {
			rules = service.getAllRulesByVersionGreaterThan(version);
		} else {
			rules = service.getAllRules();
		}

		List<RuleRest> rulesRest = new ArrayList<RuleRest>();
		for (Rule rule : rules) {
			rulesRest.add(Converter.toRest(rule));
		}

		return new ResponseEntity<List<RuleRest>>(rulesRest, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<RuleRest> createRule(@RequestBody String input) {
		RuleRest ruleRest = new Gson().fromJson(input, RuleRest.class);
		Rule rule = service.saveRule(Converter.toDomain(ruleRest));
		return new ResponseEntity<RuleRest>(Converter.toRest(rule), HttpStatus.CREATED);
	}
}
