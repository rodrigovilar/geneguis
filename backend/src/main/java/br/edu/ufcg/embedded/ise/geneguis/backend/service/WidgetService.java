package br.edu.ufcg.embedded.ise.geneguis.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.backend.Port;
import br.edu.ufcg.embedded.ise.geneguis.backend.Widget;
import br.edu.ufcg.embedded.ise.geneguis.backend.repository.WidgetRepository;

@Service
public class WidgetService {

	@Autowired
	private WidgetRepository widgetRepository;
	
	@Autowired
	private PortService portService;

	public List<Widget> getAll() {
		return widgetRepository.findAll();
	}

	public Widget getWidget(String name) {
		return widgetRepository.findOne(name);
	}

	public Widget getWidgetByName(String name) {
		List<Widget> widgets = widgetRepository.findByNameOrderByVersionDesc(name);
		Widget widget = safeGetFirst(widgets);
		return widget;
	}

	public Widget getWidgetByNameAndVersion(String name, Long version) {
		List<Widget> widgets = widgetRepository.findByNameAndVersion(name, version);
		return safeGetFirst(widgets);
	}

	private Widget safeGetFirst(List<Widget> widgets) {
		return widgets.size() > 0 ? widgets.get(0) : null;
	}
	
	public Widget saveWidget(Widget widget) {
		updateVersion(widget);
		updatePort(widget);
		return widgetRepository.saveAndFlush(widget);
	}

	private void updateVersion(Widget widget) {
		widget.setVersion(1l);
		
		Widget widgetSameName = getWidgetByName(widget.getName());
		
		if (widgetSameName != null) {
			widget.setVersion(widgetSameName.getVersion() + 1);
		}
	}

	private void updatePort(Widget widget) {
		if (widget.getRequiredPorts() != null) {
			for (Port port : widget.getRequiredPorts()) {
				
				Port existingPort = portService.getPortByName(port.getName());
				
				if (existingPort == null) {
					portService.createPort(port);
					port.getWidgets().add(widget);
				} 
			}
		}
	}

	public void clear() {
		widgetRepository.deleteAll();
	}

}