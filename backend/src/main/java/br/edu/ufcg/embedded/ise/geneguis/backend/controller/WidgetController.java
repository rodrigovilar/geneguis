package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.edu.ufcg.embedded.ise.geneguis.backend.Widget;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.WidgetService;

@Controller
@RequestMapping(value = "/widgets")
public class WidgetController {

	@Autowired
	private WidgetService service;

	public void setWidgetService(WidgetService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Widget>> getAll() {
		return new ResponseEntity<List<Widget>>(service.getAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "{widget}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Widget> getWidget(@PathVariable String widgetName) {
		Widget widget = service.getWidget(widgetName);

		if (widget == null) {
			return new ResponseEntity<Widget>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Widget>(widget, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<WidgetRest> createWidget(@RequestBody String input) {
		WidgetRest widget = new Gson().fromJson(input, WidgetRest.class);
		widget = Converter.toRest(service.saveWidget(Converter.toDomain(widget)));
		return new ResponseEntity<WidgetRest>(widget, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{widgetName}/code", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<WidgetCodeRest> createWidgetCode(@PathVariable String widgetName, @RequestBody String code) {
		WidgetCodeRest codeRest = new WidgetCodeRest();
		codeRest.setName(widgetName);
		codeRest.setCode(code);

		Widget widget = service.getWidget(widgetName);

		if (widget == null) {
			return new ResponseEntity<WidgetCodeRest>(HttpStatus.NOT_FOUND);
		}
		
		widget.setCode(code);
		service.saveWidget(widget);

		return new ResponseEntity<WidgetCodeRest>(codeRest, HttpStatus.CREATED);
	}

}