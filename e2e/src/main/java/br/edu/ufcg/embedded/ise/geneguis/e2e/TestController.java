package br.edu.ufcg.embedded.ise.geneguis.e2e;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.PortService;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.RuleService;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.WidgetService;

@Controller
@RequestMapping(value = "/test")
public class TestController {

	@Autowired
	private WidgetService widgetService;

	@Autowired
	private RuleService ruleService;
	
	@Autowired
	private PortService portService;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "reset", method = RequestMethod.GET)
	public ResponseEntity<?> reset() {
		ruleService.clear();
		portService.clear();
		widgetService.clear();
		EntryPoint.getDomainModel().clear();
		
		return new ResponseEntity(HttpStatus.OK);
	}

}