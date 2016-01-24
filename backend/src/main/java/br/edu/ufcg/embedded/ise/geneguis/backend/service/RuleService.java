package br.edu.ufcg.embedded.ise.geneguis.backend.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.backend.Port;
import br.edu.ufcg.embedded.ise.geneguis.backend.Rule;
import br.edu.ufcg.embedded.ise.geneguis.backend.Widget;
import br.edu.ufcg.embedded.ise.geneguis.backend.repository.RuleRepository;

@Service
public class RuleService {

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private PortService portService;

	@Autowired
	private WidgetService widgetService;

	private Long version;

	private void updateVersion() {
		version = 1l;
		List<Rule> rules = ruleRepository.findAll(new Sort(Sort.Direction.DESC, "version"));
		if (rules.size() > 0) {
			version = rules.get(0).getVersion();
		}
	}

	public List<Rule> getAllRules() {
		return ruleRepository.findAll();
	}

	public List<Rule> getAllRulesByVersionGreaterThan(Long version) {
		return ruleRepository.findByVersionGreaterThan(version);
	}

	@Transactional
	public Rule saveRule(@Valid Rule rule) {
		if (version == null) {
			updateVersion();
		}
		rule.setVersion(++version);

		Port port = portService.getPortByName(rule.getPort().getName());
		rule.getPort().setId(port.getId());

		Widget widget = rule.getWidget();
		if (widget.getVersion() != null && widget.getVersion() != 0l) {
			widget = widgetService.getWidgetByNameAndVersion(widget.getName(), widget.getVersion());
		} else {
			widget = widgetService.getWidgetByName(widget.getName());
		}
		rule.getWidget().setId(widget.getId());

		return ruleRepository.saveAndFlush(rule);
	}

	public void clear() {
		ruleRepository.deleteAll();
	}

}