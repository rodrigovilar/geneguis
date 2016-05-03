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
window.Filter = {}


GUI.downloadAllRules = (callback) ->
	$.getJSON HOST + 'rules', (rules) =>
		rules.forEach (rule) =>
			RulesCache.push rule
		callback()

GUI.getWidgetByRule = (rule, callback) ->
	widget = WidgetCache[rule.widgetName + rule.widgetVersion]
	widget.configuration = $.parseJSON(rule.configuration)
	console.log 'getWidgetByRule: ' + rule.portName + '->' + widget.name
	callback(widget)

GUI.getEntityTypeWidget = (portName, entityTypeName, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName

GUI.getEntityWidget = (portName, entityTypeName, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName

GUI.getPropertyTypeWidget = (portName, entityTypeName, fieldType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			if (rule.type == "PropertyType" || rule.type == "Property" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Property" 
				found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName
		
GUI.getRelationshipTypeWidget = (portName, entityTypeName, fieldType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			if (rule.type == "RelationshipType" || rule.type == "Relationship" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Relationship" 
				found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName

GUI.getFieldTypeWidget = (portName, entityTypeName, fieldType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			if (rule.type == "PropertyType" || rule.type == "Property" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Property" 
				found = rule
			if (rule.type == "RelationshipType" || rule.type == "Relationship" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Relationship" 
				found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName

GUI.getFieldWidget = (portName, entityTypeName, fieldType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			if (rule.type == "PropertyType" || rule.type == "Property") && fieldType.kind == "Property" 
				found = rule
			if (rule.type == "RelationshipType" || rule.type == "Relationship") && fieldType.kind == "Relationship" 
				found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName

GUI.getPropertyWidget = (portName, entityTypeName, fieldType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			if (rule.type == "PropertyType" || rule.type == "Property") && fieldType.kind == "Property" 
				found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName

GUI.getRelationshipWidget = (portName, entityTypeName, fieldType, callback) ->
	found = null
	RulesCache.forEach (rule) =>
		if rule.portName == portName
			if (rule.type == "RelationshipType" || rule.type == "Relationship") && fieldType.kind == "Relationship" 
				found = rule
	if found
		GUI.getWidgetByRule found, callback
	else
		console.log 'No rules found for port:' + portName
			
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
	
GUI.openApp = () ->
	GUI.newPageByPort 'root'

GUI.renderPortFilter = (port, callback) ->
	console.log 'renderPortFilter: ' + port
	GUI.getWidget port, this.ctx.name, null, (widget) =>
		if (widget.type == "EntityTypeSet")
			API.getEntitiesTypes (entitiesTypes) =>
				widget.template.render entitiesTypes, (err, html) ->
					callback(null, html)

GUI.renderNewPageFilter = (port) ->
	entityTypeName = if this.ctx.entityType then this.ctx.entityType.name else this.ctx.name
	"onClick=\"GUI.newPageByPort('" + port + "','" + entityTypeName + "'," +  this.ctx.id + ")\""

GUI.renderPostFilter = (port) ->
	"onClick=\"GUI.postAndBack('" + this.ctx.name + "');return false;\""

GUI.renderPutFilter = (port) ->
	"onClick=\"GUI.putAndBack('" + this.ctx.entityType.name + "');return false;\""

GUI.renderActionFilter = (action) ->
	"onClick=\"GUI." + action + "('" + this.ctx.entityType.name + "', " + this.ctx.id + ");GUI.refresh();\""
	
Filter.forEachEntityType = (port, callback) ->
	console.log 'forEachEntityType: ' + port
	GUI.getEntityTypeWidget port, this.ctx.name, (widget) =>
		if (widget.type != "EntityType")
			msg = 'forEachEntityType: ' + widget.name + ' is not EntityType'
			console.error msg
			callback(msg, null)
		else
			i = 0
			j = 0
			result = ""
			entityType = this.ctx[i]
			while (entityType)
				widget.template.render entityType, (err, html) ->
					result += html
					j++
					if (i == j)
						callback(null, result)
				entityType = this.ctx[++i]
			return

Filter.renderNewPageForEntityType = (portName) ->
	entityTypeName = this.ctx.name
	"onClick=\"Filter.newPageForEntityType('" + portName + "','" + entityTypeName + "')\""

Filter.newPageForEntityType = (portName, entityTypeName) ->
	console.log 'newPageForEntityType: ' + portName
	GUI.newPage portName, entityTypeName, (view) -> 
		API.getEntityType entityTypeName, (entityType) =>
			view.rootWidget.context = entityType
			view.rootWidget.template.render entityType, (err, html) ->
				view.html html
				
Filter.forEntityType = (port, callback) ->
	console.log 'forEntityType: ' + port
	GUI.getEntityWidget port, this.ctx.name, (widget) =>
		if (widget.type != "EntityType")
			msg = 'forEntityType: ' + widget.name + ' is not EntityType'
			console.error msg
			callback(msg, null)
		else
			API.getEntityType this.ctx.name, (entityType) =>
				widget.template.render entityType, (err, html) ->
					callback(err, html)
					
Filter.forEachEntity = (port, callback) ->
	console.log 'forEachEntity: ' + port
	entityTypeName = this.ctx.name
	GUI.getEntityWidget port, entityTypeName, (widget) =>
		API.getEntities entityTypeName, (entities) =>
			result = ""
			i = 1
			len = entities.length
			entities.forEach (entity) =>
				entity.entityType = this.ctx
				widget.template.render entity, (err, html) ->
					result += html
					if (i == len)
						callback(err, result)
					i++;
	
Filter.forEachPropertyType = (port, callback) ->
	console.log 'forEachPropertyType: ' + port
	entityType = this.ctx
	result = ""
	i = 1
	len = entityType.fieldTypes.length
	entityType.fieldTypes.forEach (fieldType) ->
		if fieldType.kind == "Property"
			GUI.getPropertyTypeWidget port, entityType.name, fieldType, (widget) ->
				fieldType.entity = entityType
				widget.template.render fieldType, (err, html) ->
					result += html
					if (i == len)
						callback(null, result)
					i++;
					
Filter.forEachRelationshipType = (port, callback) ->
	console.log 'forEachRelationshipType: ' + port
	entityType = this.ctx
	result = ""
	i = 1
	len = entityType.fieldTypes.length
	entityType.fieldTypes.forEach (fieldType) ->
		if fieldType.kind == "Relationship"
			GUI.getRelationshipTypeWidget port, entityType.name, fieldType, (widget) ->
				fieldType.entity = entityType
				widget.template.render fieldType, (err, html) ->
					result += html
					if (i == len)
						callback(null, result)
					i++;
				
Filter.forEachFieldType = (port, callback) ->
	console.log 'forEachFieldType: ' + port
	entityType = this.ctx
	result = ""
	i = 1
	len = entityType.fieldTypes.length
	entityType.fieldTypes.forEach (fieldType) ->
		GUI.getFieldTypeWidget port, entityType.name, fieldType, (widget) ->
			fieldType.entity = entityType
			widget.template.render fieldType, (err, html) ->
				result += html
				if (i == len)
					callback(null, result)
				i++;

Filter.forEachProperty = (port, callback) ->
	console.log 'forEachProperty: ' + port
	entity = this.ctx
	entityType = if this.ctx.entityType then this.ctx.entityType else this.ctx.type
	result = ""
	i = 1
	len = entityType.fieldTypes.length
	entityType.fieldTypes.forEach (fieldType) ->
		if fieldType.kind == "Property"
			GUI.getPropertyWidget port, entityType.name, fieldType, (widget) ->
				fieldType.entity = entityType
				propertyValue = entity[fieldType.name]
				property = {value: propertyValue, type: fieldType}
				widget.template.render property, (err, html) ->
					result += html
					if (i == len)
						callback(null, result)
					i++;

Filter.forEachRelationship = (port, callback) ->
	console.log 'forEachRelationship: ' + port
	entity = this.ctx
	entityType = if this.ctx.entityType then this.ctx.entityType else this.ctx.type
	result = ""
	i = 1
	len = entityType.fieldTypes.length
	entityType.fieldTypes.forEach (fieldType) ->
		if fieldType.kind == "Relationship"
			GUI.getRelationshipWidget port, entityType.name, fieldType, (widget) ->
				fieldType.entity = entityType
				relationshipId = entity[fieldType.name]
				API.getEntity fieldType.targetType, relationshipId, (entity) =>
					relationship = {target: entity, type: fieldType}
					widget.template.render relationship, (err, html) ->
						result += html
						if (i == len)
							callback(null, result)
						i++;
									
Filter.forEachField = (port, callback) ->
	console.log 'forEachField: ' + port
	entity = this.ctx
	entityType = if this.ctx.entityType then this.ctx.entityType else this.ctx.type
	result = ""
	i = 1
	len = entityType.fieldTypes.length
	entityType.fieldTypes.forEach (fieldType) ->
		GUI.getFieldWidget port, entityType.name, fieldType, (widget) ->
			fieldType.entity = entityType
			field = null
			if fieldType.kind == "Property"
				propertyValue = entity[fieldType.name]
				field = {value: propertyValue, type: fieldType}		
				widget.template.render field, (err, html) ->
					result += html
					if (i == len)
						callback(null, result)
					i++;
			else
				relationshipId = entity[fieldType.name]
				API.getEntity fieldType.targetType, relationshipId, (entity) =>
					field = {target: entity, type: fieldType}
					widget.template.render field, (err, html) ->
						result += html
						if (i == len)
							callback(null, result)
						i++;


Filter.renderNewPageForEntity = (portName) ->
	entityTypeName = this.ctx.entityType.name
	"onClick=\"Filter.newPageForEntity('" + portName + "','" + entityTypeName + "'," +  this.ctx.id + ")\""

Filter.newPageForEntity = (portName, entityTypeName, entityId) ->
	console.log 'newPageForEntity: ' + portName
	GUI.newPage portName, entityTypeName, (view) -> 
		API.getEntityType entityTypeName, (entityType) =>
			API.getEntity entityTypeName, entityId, (entity) =>
				entity.entityType = entityType
				view.rootWidget.context = entity
				view.rootWidget.template.render entity, (err, html) ->
					view.html html

$ ->
	env.addFilter('forEachEntityType', Filter.forEachEntityType, true)
	env.addFilter('newPageForEntityType', Filter.renderNewPageForEntityType, false)
	env.addFilter('forEntityType', Filter.forEntityType, true)
	env.addFilter('forEachEntity', Filter.forEachEntity, true)
	env.addFilter('newPageForEntity', Filter.renderNewPageForEntity, false)
	env.addFilter('forEachPropertyType', Filter.forEachPropertyType, true)
	env.addFilter('forEachProperty', Filter.forEachProperty, true)
	env.addFilter('forEachFieldType', Filter.forEachFieldType, true)
	env.addFilter('forEachField', Filter.forEachField, true)
	env.addFilter('post', GUI.renderPostFilter, false)
	env.addFilter('put', GUI.renderPutFilter, false)
	env.addFilter('action', GUI.renderActionFilter, false)
	GUI.downloadAllRules () ->
		GUI.downloadAllWidgets () ->
			GUI.openApp()