nunjucks.configure({ autoescape: false });
window.env = new nunjucks.Environment(null, { autoescape: false })

Date.prototype.toISO8601 = () =>
	`self = this`
	if(!isNaN(self))
		`self.toISOString().slice(0, 19) + 'Z'`

Date.prototype.toJSON = () =>
	`this.toISO8601()`

window.HOST = 'http://localhost:8080/'

window.RulesCache = []
window.WidgetCache = {}
window.WidgetStack = []
window.GUI = {}
window.API = {}


GUI.downloadAllRules = (callback) ->
	$.getJSON HOST + 'rules', (rules) =>
		rules.forEach (rule) =>
			RulesCache.push rule
		callback()
			
GUI.getWidget = (portName, entityTypeName, propertyType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName 
			found = rule
	if found
		widget = WidgetCache[found.widgetName + found.widgetVersion]
		widget.configuration = $.parseJSON(found.configuration)
		console.log 'getWidget: ' + portName + '->' + widget.name
		callback(widget)

GUI.downloadAllWidgets = (callback) ->
	$.getJSON HOST + 'widgets', (widgetSpecs) =>
		widgetSpecs.forEach (widgetSpec) =>
			widgetSpec.template = new nunjucks.Template(widgetSpec.code, window.env, null, true)
			WidgetCache[widgetSpec.name + widgetSpec.version] = widgetSpec
		callback()

GUI.newPage = (portName, entityTypeName, callback) ->
	body  = $("body")
	body.empty()  
	page = $('<div>')
	page.attr "id", "page_view"
	body.append page
	GUI.getWidget portName, entityTypeName, null, (widget) ->
		page.rootWidget = widget
		page.rootWidget.context = { name: entityTypeName }
		WidgetStack.push page
		callback(page)
		
GUI.back = () ->
	WidgetStack.pop()
	GUI.refresh()
		
GUI.refresh = () ->
	body  = $("body")
	body.empty()  
	page = $('<div>')
	page.attr "id", "page_view"
	body.append page
	widget = WidgetStack[WidgetStack.length - 1].rootWidget
	widget.template.render widget.context, (err, html) ->
		page.html html

API.loadData = (url, callback) ->
	$.getJSON HOST + url, (json) =>
			callback(json)

API.getEntitiesTypes = (callback) ->
	API.loadData 'entities', callback

API.getEntityType = (entityId, callback) ->
	API.loadData 'entities/' + entityId, callback

API.getEntities = (entityTypeResource, callback) ->
	API.loadData 'api/' + entityTypeResource, (entities) =>
		callback(entities)

API.getEntity = (entityTypeResource, entityID, callback) ->
	API.loadData 'api/' + entityTypeResource + '/' + entityID, callback
	
API.createEntity = (entityTypeResource, entity, callback) =>
	$.ajax
		url: HOST + "api/" + entityTypeResource
		type: "POST"
		data: JSON.stringify(entity)
		contentType: "application/json; charset=utf-8"
	.done () ->
		callback()

API.updateEntity = (entityTypeResource, entity, callback) =>
	$.ajax
		url: HOST + "api/" + entityTypeResource + "/" + entity.id
		type: "PUT"
		data: JSON.stringify(entity)
		contentType: "application/json; charset=utf-8"
	.done () ->
		callback()

API.deleteEntity = (entityTypeResource, entityID, success, error) =>
	$.ajax
		url: HOST + "api/" + entityTypeResource + "/" + entityID
		type: "DELETE"

GUI.postAndBack = (entityTypeName) ->
	console.log 'postAndBack: ' + entityTypeName
	formData = $("#create_" + entityTypeName).serializeArray()
	entity = {}
	formData.forEach (field) =>
		entity[field.name.substring(18)] = if (field.value == "") then null else field.value
	API.createEntity entityTypeName, entity, () ->
		console.log 'POST OK: ' + entityTypeName + ' ' + JSON.stringify(entity)
		GUI.back()

GUI.putAndBack = (entityTypeName) ->
	console.log 'putAndBack: ' + entityTypeName
	formData = $("#edit_" + entityTypeName).serializeArray()
	entity = {}
	formData.forEach (field) =>
		entity[field.name.substring(16)] = if (field.value == "") then null else field.value
	API.updateEntity entityTypeName, entity, () ->
		console.log 'PUT OK: ' + entityTypeName + ' ' + JSON.stringify(entity)
		GUI.back()
		
GUI.remove = (entityTypeName, entityID) ->
	API.deleteEntity(entityTypeName, entityID)

GUI.newPageByPort = (portName, entityTypeName, entityId) ->
	console.log 'newPageByPort: ' + portName
	GUI.newPage portName, entityTypeName, (view) -> 
		if (view.rootWidget.type == "EntityTypeSet")
			API.getEntitiesTypes (entitiesTypes) =>
				view.rootWidget.context = entitiesTypes
				view.rootWidget.template.render entitiesTypes, (err, html) ->
					view.html html
		if (view.rootWidget.type == "EntityType")
			API.getEntityType entityTypeName, (entityType) =>
				view.rootWidget.context = entityType
				view.rootWidget.template.render entityType, (err, html) ->
					view.html html
		if (view.rootWidget.type == "Entity")
			API.getEntityType entityTypeName, (entityType) =>
				API.getEntity entityTypeName, entityId, (entity) =>
					entity.entityType = entityType
					view.rootWidget.context = entity
					view.rootWidget.template.render entity, (err, html) ->
						view.html html
	
GUI.openApp = () ->
	GUI.newPageByPort 'root'

GUI.renderPortFilter = (port, callback) ->
	console.log 'renderPortFilter: ' + port
	GUI.getWidget port, this.ctx.name, null, (widget) =>
		if (widget.type == "EntityTypeSet")
			API.getEntitiesTypes (entitiesTypes) =>
				widget.template.render entitiesTypes, (err, html) ->
					callback(null, html)
		if (widget.type == "EntityType")
			if (this.ctx.name)
				API.getEntityType this.ctx.name, (entityType) =>
					widget.template.render entityType, (err, html) ->
						callback(null, html)
			else
				i = 0
				j = 0
				result = ""
				while true
					entityType = this.ctx[i]
					if (entityType)
						widget.template.render entityType, (err, html) ->
							result += html
							j++
							if (i == j)
								callback(null, result)
					else
						return
					i++
		if (widget.type == "Entity")
			API.getEntities this.ctx.name, (entities) =>
				result = ""
				i = 1
				len = entities.length
				entities.forEach (entity) =>
					entity.entityType = this.ctx
					widget.template.render entity, (err, html) ->
						result += html
						if (i == len)
							callback(null, result)
						i++;
		if (widget.type == "PropertyType")
			entityType = this.ctx
			result = ""
			i = 1
			len = entityType.propertyTypes.length
			entityType.propertyTypes.forEach (propertyType) ->
				GUI.getWidget port, entityType.name, propertyType, (widget) ->
					propertyType.entity = entityType
					widget.template.render propertyType, (err, html) ->
						result += html
						if (i == len)
							callback(null, result)
						i++;
		if (widget.type == "Property")
			entity = this.ctx
			entityType = if this.ctx.entityType then this.ctx.entityType else this.ctx.type
			result = ""
			i = 1
			len = entityType.propertyTypes.length
			entityType.propertyTypes.forEach (propertyType) ->
				GUI.getWidget port, entityType.name, propertyType, (widget) ->
					propertyType.entity = entityType
					propertyValue = entity[propertyType.name]
					property = {value: propertyValue, type: propertyType}
					widget.template.render property, (err, html) ->
						result += html
						if (i == len)
							callback(null, result)
						i++;	

GUI.renderNewPageFilter = (port) ->
	entityTypeName = if this.ctx.entityType then this.ctx.entityType.name else this.ctx.name
	"onClick=\"GUI.newPageByPort('" + port + "','" + entityTypeName + "'," +  this.ctx.id + ")\""

GUI.renderPostFilter = (port) ->
	"onClick=\"GUI.postAndBack('" + this.ctx.name + "');return false;\""

GUI.renderPutFilter = (port) ->
	"onClick=\"GUI.putAndBack('" + this.ctx.entityType.name + "');return false;\""

GUI.renderActionFilter = (action) ->
	"onClick=\"GUI." + action + "('" + this.ctx.entityType.name + "', " + this.ctx.id + ");GUI.refresh();\""

$ ->
	env.addFilter('port', GUI.renderPortFilter, true)
	env.addFilter('newPage', GUI.renderNewPageFilter, false)
	env.addFilter('post', GUI.renderPostFilter, false)
	env.addFilter('put', GUI.renderPutFilter, false)
	env.addFilter('action', GUI.renderActionFilter, false)
	GUI.downloadAllRules () ->
		GUI.downloadAllWidgets () ->
			GUI.openApp()
