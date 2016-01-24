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

	public Widget getWidget(Long id) {
		return widgetRepository.findOne(id);
	}

	public Widget getWidgetByName(String name) {
		List<Widget> widgets = widgetRepository.findByNameOrderByVersionDesc(name);
		Widget widget = safeGetFirst(widgets);
		return widget;
	}

	public Widget getWidgetByNameAndVersion(String name, Long version) {
		List<Widget> widgets = widgetRepository.findByNameAndVersion(name, version);
		Widget widget = safeGetFirst(widgets);
		return widget;
	}

	private Widget safeGetFirst(List<Widget> widgets) {
		Widget widget = widgets.size() > 0 ? widgets.get(0) : null;
		return widget;
	}
	
	public Widget saveWidget(Widget widget) {
		widget.setVersion(1l);
		
		Widget widgetSameName = getWidgetByName(widget.getName());
		if (widgetSameName != null) {
			widget.setVersion(widgetSameName.getVersion() + 1);
		}
		
		if (widget.getRequiredPorts() != null) {
			for (Port context : widget.getRequiredPorts()) {
				context.setId(portService.createPort(context).getId());
			}
		}
		
		return widgetRepository.saveAndFlush(widget);
	}

	public void clear() {
		widgetRepository.deleteAll();
	}

}