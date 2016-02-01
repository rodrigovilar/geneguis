class window.RootRenderer

	render: (view, entitesType) ->
		table = $("<table>")
		view.append table
		th = $("<tr><th>Entities</th></tr>")
		th.attr "id", "entities"
		table.append th
		entitesType.forEach (entityType) =>
			@drawLine(table, entityType)

	drawLine: (table, entityType) ->
		tr = $("<tr><td>#{entityType.name}</td></tr>")
		tr.attr "id", "entityType_" + entityType.name
		table.append tr
		tr.click =>
			widget = RenderingEngine.getEntitySetWidget 'root', entityType.name 
			DataManager.getEntityType entityType.name, (entityTypeFull) =>
				div = View.emptyPage()
				html = widget.render entityTypeFull
				div.append html
			
				
window.ID = ->
  '_' + Math.random().toString(36).substr(2, 9)

window.View = {}

View.emptyPage = ->
	body  = $("body")
	body.empty()  
	page = $('<div>')
	page.attr "id", "page_view"
	body.append page
	page
	
	
View.createEl = (tag, prefix, value) ->
	el = $(tag)
	el.attr "id", prefix + "_" + value
	el

		
window.WidgetManager = {}

WidgetManager.STORAGE_TAG = "WIDGETS"

WidgetManager.downloadAllWidgets = () ->
	$.getJSON HOST + 'widgets', (widgetsSpec) =>
		widgetsSpec.forEach (widgetSpec) =>
			simpleStorage.set(WidgetManager.STORAGE_TAG + widgetSpec.name + widgetSpec.version, widgetSpec)

WidgetManager.getWidget = (name, version) ->
	widget = simpleStorage.get(WidgetManager.STORAGE_TAG + name + version)
	widget.render = Handlebars.compile( widget.code )
	widget

WidgetManager.getRootRenderer = (callback) ->
	callback(new RootRenderer)
	
	
window.DataManager = {}

DataManager.loadData = (url, callback) ->
	$.getJSON HOST + url, (json) =>
			callback(json)

DataManager.getAllEntitiesTypes = (callback) ->
	DataManager.loadData 'entities', callback

DataManager.getEntityType = (entityId, callback) ->
	DataManager.loadData 'entities/' + entityId, callback

DataManager.getEntities = (entityTypeResource, callback) ->
	DataManager.loadData 'api/' + entityTypeResource, (entities) =>
		callback(entities)

DataManager.getEntity = (entityTypeResource, entityID, callback) ->
	DataManager.loadData 'api/' + entityTypeResource + '/' + entityID, callback

DataManager.createEntity = (entityTypeResource, entity) =>
	$.ajax
		url: HOST + "api/" + entityTypeResource
		type: "POST"
		data: JSON.stringify(entity)
		contentType: "application/json; charset=utf-8"

DataManager.updateEntity = (entityTypeResource, entity) =>
	$.ajax
		url: HOST + "api/" + entityTypeResource + "/" + entity.id
		type: "PUT"
		data: JSON.stringify(entity)

DataManager.deleteEntity = (entityTypeResource, entityID, success, error) =>
	$.ajax
		url: HOST + "api/" + entityTypeResource + "/" + entityID
		type: "DELETE"
	
	
window.RulesManager = {}

RulesManager.STORAGE_TAG = "RULES"
RulesManager.SEPARATOR_CHAR = '.'

RulesManager.stringfyRule = (rule) =>
	stringRule = RulesManager.STORAGE_TAG + RulesManager.SEPARATOR_CHAR + rule.portName
	if(rule.entityTypeLocator != null)
			stringRule += RulesManager.SEPARATOR_CHAR + rule.entityTypeLocator
		else
			stringRule += RulesManager.SEPARATOR_CHAR + '*'
	if(rule.type == 'Property' || rule.type == 'Relationship')
		if(rule.propertyTypeLocator != null)
			stringRule += RulesManager.SEPARATOR_CHAR + rule.propertyTypeLocator
		else
			stringRule += RulesManager.SEPARATOR_CHAR + '*'
		if(rule.propertyTypeTypeLocator != null)
			stringRule += RulesManager.SEPARATOR_CHAR + rule.propertyTypeTypeLocator
		else
			stringRule += RulesManager.SEPARATOR_CHAR + '*'
		if(rule.type == 'Relationship')
			if(rule.relationshipTargetCardinality != null)
				stringRule += RulesManager.SEPARATOR_CHAR + rule.relationshipTargetCardinality
			else
				stringRule += RulesManager.SEPARATOR_CHAR + '*'
	stringRule

RulesManager.createRule = (contextName, contextType, entityTypeLocator, propertyTypeLocator, propertyTypeTypeLocator, relationshipTargetCardinality) =>
	rule = {}
	rule.portName = contextName
	rule.type = contextType
	rule.entityTypeLocator = entityTypeLocator
	rule.propertyTypeLocator = propertyTypeLocator
	if(entityTypeLocator == null || propertyTypeLocator == null)
		rule.propertyTypeTypeLocator = propertyTypeTypeLocator
		rule.relationshipTargetCardinality = relationshipTargetCardinality
	rule

RulesManager.downloadAllRules = () ->
	$.getJSON HOST + 'rules', (rules) =>
		rules.forEach (rule) =>
			if(rule.type == 'Property' || rule.type == 'Relationship')
				if(rule.entityTypeLocator != null && rule.propertyTypeLocator != null)
					rule.propertyTypeTypeLocator = null
					rule.relationshipTargetCardinality = null
			key = RulesManager.stringfyRule(rule)
			simpleStorage.set(key, rule)

RulesManager.getRule = (contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality) =>
	ruleQuery = RulesManager.createRule(contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality)
	if(ruleQuery.type == 'Property')
	
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, propertyTypeType, null)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, null, null)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, propertyTypeType, null)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, null, null)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, null, propertyTypeType, null)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, null, null, null)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
	
	else if(ruleQuery.type == 'Relationship')
	
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, propertyTypeType, relationshipTargetCardinality)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, null, relationshipTargetCardinality)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, propertyTypeType, relationshipTargetCardinality)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, null, relationshipTargetCardinality)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, null, propertyTypeType, relationshipTargetCardinality)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery = RulesManager.createRule(contextName, contextType, null, null, null, relationshipTargetCardinality)
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
	else
		
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
		
		ruleQuery.entityTypeLocator = '*'
		rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery))
		if(typeof rule != "undefined")
			return rule
	
	
window.HOST = 'http://localhost:8080/'
window.RenderingEngine = {}
window.WidgetStack = []

Date.prototype.toISO8601 = () =>
	`self = this`
	if(!isNaN(self))
		`self.toISOString().slice(0, 19) + 'Z'`

Date.prototype.toJSON = () =>
	`this.toISO8601()`
	
RenderingEngine.pushWidget = (widget) ->
	WidgetStack.push(widget)

RenderingEngine.popAndRender = (view) ->
	WidgetStack.pop().render(view)

RenderingEngine.openApp = (view) ->
	WidgetManager.getRootRenderer (rootRenderer) =>
		DataManager.getAllEntitiesTypes (allEntitiesTypes) =>
			rootRenderer.render view, allEntitiesTypes

RenderingEngine.getWidget = (contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality) =>
	rule = RulesManager.getRule(contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality)
	widget = WidgetManager.getWidget(rule.widgetName, rule.widgetVersion)
	widget.configuration = $.parseJSON(rule.configuration)
	widget

RenderingEngine.getRelationshipWidget = (context, entityTypeName, relationshipType) =>
	RenderingEngine.getWidget(context, 'Relationship', entityTypeName, relationshipType.name, relationshipType.targetType.name, relationshipType.targetCardinality)

RenderingEngine.getPropertyWidget = (context, entityTypeName, propertyType) =>
	RenderingEngine.getWidget(context, 'Property', entityTypeName, propertyType.name, propertyType.type, null)

RenderingEngine.getEntityWidget = (context, entityTypeName) =>
	RenderingEngine.getWidget(context, 'Entity', entityTypeName, null, null, null)

RenderingEngine.getEntitySetWidget = (context, entityTypeName) =>
	RenderingEngine.getWidget(context, 'EntitySet', entityTypeName, null, null, null)

$ ->
	Handlebars.registerHelper('renderEntitySet', RenderingEngine.renderEntitySet)
	Handlebars.registerHelper('renderEntities', RenderingEngine.renderEntities)
	$.getScript "https://dl.dropboxusercontent.com/u/14874989/Mestrado/metaguiweb/js/simpleStorage.js", () =>
		$.getScript "https://dl.dropboxusercontent.com/u/14874989/mestrado/metaguiweb/js/jquery.mask.min.js", () =>
			RulesManager.downloadAllRules()
			WidgetManager.downloadAllWidgets()
			RenderingEngine.openApp View.emptyPage()


RenderingEngine.tempDiv = (viewId) ->
	new Handlebars.SafeString("<div id='" + viewId + "'></div>")

RenderingEngine.populateTempDiv = (viewId, el) ->
		div = $("#" + viewId)
		div.html el

RenderingEngine.appendTempDiv = (viewId, el) ->
		div = $("#" + viewId)
		div.append el
			
RenderingEngine.renderEntitySet = (port, entityTypeName) =>
	console.log 'renderEntitySet: ' + port + ', ' + entityTypeName
	viewId = ID()
	widget = RenderingEngine.getEntitySetWidget port, entityTypeName 
	DataManager.getEntityType entityTypeName, (entityTypeFull) =>
		RenderingEngine.populateTempDiv viewId, widget.render(entityTypeFull)
	RenderingEngine.tempDiv viewId

RenderingEngine.renderEntities = (port, entityTypeName) =>
	console.log 'renderEntities: ' + port + ', ' + entityTypeName
	viewId = ID()
	widget = RenderingEngine.getEntityWidget port, entityTypeName 
	DataManager.getEntityType entityTypeName, (entityTypeFull) =>
		DataManager.getEntities entityTypeName, (entities) =>
			entities.forEach (entity) ->
				entity.type = entityTypeFull
				RenderingEngine.appendTempDiv viewId, widget.render(entity)
	RenderingEngine.tempDiv viewId
					
class window.EntitySetWidget

	render: (entityType) ->
		
		
class window.EntitySetTemplate extends EntitySetWidget

	render: (entityType) ->
		console.log @constructor.name + ': ' + entityType.name
		t = Handlebars.compile( @template() )
		t( entityType )
			
	template: () ->
		
		
class window.EntityWidget

	render: (entityType, entity) ->
	

class window.EntityTemplate extends EntityWidget

	render: (entityType, entity) ->
		console.log @constructor.name + ': ' + entityType.name
		t = Handlebars.compile( @template() )
		t( {entityType: entityType, entity: entity} )
			
	template: () ->

	
class window.PropertyWidget

	render: (view) ->
		
	injectValue: (entity) ->
		
	createInputElement: () =>
		input = $("<input>")
		input.attr "id", @propertyType.name
		input
		

class window.RelationshipWidget

	render: (view, relationType, relation) ->
		
	populateSelectField: (selectField, resource, propertyKey, relationshipIds) ->
		DataManager.getEntities resource, (entities) =>
			entities.forEach (entity) ->			
				option = new Option(entity.id)
				if(propertyKey)
					option = new Option(entity[propertyKey], entity.id)
				selectField.append option
				if(relationshipIds && relationshipIds.indexOf(entity.id) != -1)
					option.selected = true

