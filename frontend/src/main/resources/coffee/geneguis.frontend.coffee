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
	widget

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

GUI.newPage = (widget, entityTypeName) ->
  body  = $("body")
  body.empty()  
  page = $('<div>')
  page.attr "id", "page_view"
  body.append page
  page.rootWidget = widget
  page.rootWidget.context = { name: entityTypeName }
  WidgetStack.push page
  page
		
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
		if err
			throw err
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

GUI.newPageForEntityTypeSet = (port, entityTypeName, entityId) ->
  console.log 'newPageForEntityTypeSet: ' + port
  GUI.getWidget port, entityTypeName, null, (widget) ->
    view = GUI.newPage widget, entityTypeName 
    if (view.rootWidget.type == "EntityTypeSet")
      API.getEntitiesTypes (entitiesTypes) =>
        view.rootWidget.context = entitiesTypes
        view.rootWidget.template.render entitiesTypes, (err, html) ->
          if err
            throw err
          view.html html
	
GUI.openApp = () ->
	GUI.newPageForEntityTypeSet 'root'

GUI.renderPostFilter = (port) ->
	"onClick=\"GUI.postAndBack('" + this.ctx.name + "');return false;\""

GUI.renderPutFilter = (port) ->
	"onClick=\"GUI.putAndBack('" + this.ctx.entityType.name + "');return false;\""

GUI.renderActionFilter = (action) ->
	"onClick=\"GUI." + action + "('" + this.ctx.entityType.name + "', " + this.ctx.id + ");GUI.refresh();\""
						
class Filter.AbstractFilter
  constructor: (@name) ->
  
  renderNewPage: (port, context) ->
    entityTypeName = context.name
    "onClick=\"Filter.newPageFor#{@name}('#{port}','#{entityTypeName}')\""

  forEachPort: (port, context, callback) ->

  forPort: (port, context, callback) ->
    console.log @name + '.forPort: ' + port
    widget = @getWidget port, context
    @renderFor widget, context, callback

  getWidget: (port, params) ->
    rule = @getRule port, params
    @checkRule rule
    GUI.getWidgetByRule rule  
  
  getRule: (port, params) ->

  checkRule: (rule) ->
    if rule
      if (rule.type == @name)
        return
      else
        throw 'Problem on port #{port}: #{rule.name} is not #{@name}'
    else
      throw 'No rules found for port: #{port}'

  renderFor: (widget, context, callback) ->
  
  matchExpression: (text, expression) -> 
    if expression
      return new RegExp("^" + expression.split("*").join(".*") + "$").test(text);
    return false
    
  getTag: (tag, tags) -> 
    if tags
      for t in tags
        if t.name == tag
          return t
    return null
    
class Filter.EntityTypeFilter extends Filter.AbstractFilter
  constructor: () ->
    super("EntityType")

  newPage: (port, entityType) ->
    console.log @name + '.newPage: ' + port
    widget = @getWidget port, {entityTypeName: entityType.name, tags: entityType.tags}
    view = GUI.newPage widget, entityType.name 
    view.rootWidget.context = entityType
    view.rootWidget.template.render entityType, (err, html) ->
      if err
        throw err
      view.html html

  getRule: (port, params) ->
    defaultScope = null
    matchTag = null
    matchName = null
    for rule in RulesCache
      tag = @getTag(rule.tag, params.tags)    
      if rule.portName == port
        if rule.entityTypeLocator == "*"
          defaultScope = rule
        else if tag
          matchTag = rule
          if tag.value
            params[tag.name + "Value"] = tag.value
        else if @matchExpression(params.entityTypeName, rule.entityTypeLocator)
          matchName = rule
    if matchName
      return matchName
    if matchTag
      return matchTag
    return defaultScope

  forEachPort: (port, context, callback) ->
    console.log @name + '.forEachPort: ' + port
    size = 0
    result = ""
    current = 0
    item = context[size]
    while item
      widget = @getWidget port, {entityTypeName: item.name, tags: item.tags}
      widget.template.render item, (err, html) ->
        if err
          throw err
        result += html
        if (++current == size)
          callback(null, result)
      item = context[++size]

  renderFor: (widget, context, callback) ->
    API.getEntityType context.name, (entityType) =>
      widget.template.render entityType, (err, html) ->
        if err
          throw err
        callback(null, html)

class Filter.EntityFilter extends Filter.AbstractFilter
  constructor: () ->
    super("Entity")

  renderNewPage: (port, context) ->
    entityTypeName = context.name
    "onClick=\"Filter.newPageFor#{@name}('#{port}','#{entityTypeName}',#{context.id})\""

  newPage: (port, entityType, entity) ->
    console.log @name + '.newPage: ' + port
    widget = @getWidget port, {entityTypeName: entityType.name, tags: entityType.tags}
    view = GUI.newPage widget, entityTypeName 
    entity.entityType = entityType
    view.rootWidget.context = entity
    view.rootWidget.template.render entity, (err, html) ->
      if err
        throw err
      view.html html

  getRule: (port, params) ->
    defaultScope = null
    matchTag = null
    matchName = null
    for rule in RulesCache
      tag = @getTag(rule.tag, params.tags)    
      if rule.portName == port
        if rule.entityTypeLocator == "*"
          defaultScope = rule
        else if tag
          matchTag = rule
          if tag.value
            params[tag.name + "Value"] = tag.value
        else if @matchExpression(params.entityTypeName, rule.entityTypeLocator)
          matchName = rule
    if matchName
      return matchName
    if matchTag
      return matchTag
    return defaultScope

  forEachPort: (port, context, callback) ->
    console.log @name + '.forEachPort: ' + port
    widget = @getWidget port, {entityTypeName: context.name, tags: context.tags}
    API.getEntities context.name, (entities) =>
      result = ""
      size = entities.length
      current = 0
      for entity in entities
        entity.entityType = context
        widget.template.render entity, (err, html) ->
          if err
            throw err
          result += html
          if (++current == size)
            callback(null, result)
          
  renderFor: (widget, context, callback) ->
    API.getEntityType context.name, (entityType) =>
      widget.template.render entityType, (err, html) ->
        if err
          throw err
        callback(null, html)

class Filter.PropertyTypeFilter extends Filter.AbstractFilter
  constructor: () ->
    super("PropertyType")

  getRule: (port, params) ->
    fieldType = params.fieldType
    defaultScope = null
    matchTag = null
    matchScope = null
    matchType = null
    for rule in RulesCache
      tag = @getTag(rule.tag, params.tags)    
      if rule.portName == port
        if (rule.type == "PropertyType" || rule.type == "Property") && fieldType.kind == "Property" 
          if rule.propertyTypeTypeLocator 
            if @matchExpression(params.entityTypeName, rule.entityTypeLocator) && @matchExpression(fieldType.name, rule.propertyTypeLocator) && rule.propertyTypeTypeLocator == fieldType.type 
              matchType = rule
          else 
            if rule.entityTypeLocator == "*" & rule.propertyTypeLocator == "*"
              defaultScope = rule
            else if tag
              matchTag = rule
              if tag.value
                params[tag.name + "Value"] = tag.value
            else if @matchExpression(params.entityTypeName, rule.entityTypeLocator) && @matchExpression(fieldType.name, rule.propertyTypeLocator)
              matchScope = rule
    if matchType
      return matchType
    if matchScope
      return matchScope
    if matchTag
      return matchTag
    return defaultScope

  forEachPort: (port, entityType, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    size = entityType.fieldTypes.length
    for fieldType in entityType.fieldTypes
      if fieldType.kind == "Property"
        widget = @getWidget port, {entityTypeName: entityType.name, fieldType: fieldType, tags: fieldType.tags}
        fieldType.entity = entityType
        widget.template.render fieldType, (err, html) ->
          if err
            throw err
          result += html
          if (++current == size)
            callback(null, result)

class Filter.RelationshipTypeFilter extends Filter.AbstractFilter
  constructor: () ->
    super("RelationshipType")

  getRule: (port, params) ->
    fieldType = params.fieldType
    found = null
    for rule in RulesCache
      if rule.portName == port
        if (rule.type == "RelationshipType" || rule.type == "Relationship" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Relationship" 
          found = rule
    return found

  forEachPort: (port, entityType, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    size = entityType.fieldTypes.length
    for fieldType in entityType.fieldTypes
      if fieldType.kind == "Relationship"
        widget = @getWidget port, {entityTypeName: entityType.name, fieldType: fieldType}
        fieldType.entity = entityType
        widget.template.render fieldType, (err, html) ->
          if err
            throw err
          result += html
          if (++current == size)
            callback(null, result)

class Filter.FieldTypeFilter extends Filter.AbstractFilter
  constructor: () ->
    super("FieldType")

  getRule: (port, params) ->
    fieldType = params.fieldType
    found = null
    for rule in RulesCache
      if rule.portName == port
        if (rule.type == "PropertyType" || rule.type == "Property" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Property" 
          found = rule
        if (rule.type == "RelationshipType" || rule.type == "Relationship" || rule.type == "FieldType" || rule.type == "Field") && fieldType.kind == "Relationship" 
          found = rule
    return found

  forEachPort: (port, entityType, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    size = entityType.fieldTypes.length
    for fieldType in entityType.fieldTypes
      if fieldType.kind == "Property"
        widget = @getWidget port, {entityTypeName: entityType.name, fieldType: fieldType}
        fieldType.entity = entityType
        widget.template.render fieldType, (err, html) ->
          if err
            throw err
          result += html
          if (++current == size)
            callback(null, result)
            
class Filter.EnumTypeFilter extends Filter.AbstractFilter
  constructor: () ->
    super("EnumerationValue")

  getRule: (port, params) ->
    fieldType = params.fieldType
    defaultScope = null
    matchScope = null
    matchType = null
    for rule in RulesCache
      if rule.portName == port
        if (rule.type == "EnumerationValue") && fieldType.kind == "Property" 
          if rule.propertyTypeTypeLocator 
            if @matchExpression(params.entityTypeName, rule.entityTypeLocator) && @matchExpression(fieldType.name, rule.propertyTypeLocator) && rule.propertyTypeTypeLocator == "enumeration" 
              matchType = rule
          else 
            if rule.entityTypeLocator == "*" & rule.propertyTypeLocator == "*"
              defaultScope = rule
            else if @matchExpression(params.entityTypeName, rule.entityTypeLocator) && @matchExpression(fieldType.name, rule.propertyTypeLocator)
              matchScope = rule
    if matchType
      return matchType
    if matchScope
      return matchScope
    return defaultScope

  forEachPort: (port, enumType, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    
    console.log '>>> Nome do enum is: ' + enumType.class
    
    size = enumType.options.length
    for option in enumType.options
      widget = @getWidget port, {entityTypeName: enumType.entity.name, fieldType: enumType}
      option.enum = enumType
      widget.template.render option, (err, html) ->
        if err
          throw err
        result += html
        if (++current == size)
          callback(null, result)

class Filter.PropertyFilter extends Filter.AbstractFilter
  constructor: () ->
    super("Property")

  getRule: (port, params) ->
    fieldType = params.fieldType
    defaultScope = null
    matchTag = null
    matchScope = null
    matchType = null
    for rule in RulesCache
      tag = @getTag(rule.tag, params.tags)    
      if rule.portName == port
        if (rule.type == "PropertyType" || rule.type == "Property") && fieldType.kind == "Property" 
          if rule.propertyTypeTypeLocator 
            if @matchExpression(params.entityTypeName, rule.entityTypeLocator) && @matchExpression(fieldType.name, rule.propertyTypeLocator) && rule.propertyTypeTypeLocator == fieldType.type 
              matchType = rule
          else 
            if rule.entityTypeLocator == "*" & rule.propertyTypeLocator == "*"
              defaultScope = rule
            else if tag
              matchTag = rule
              if tag.value
                params[tag.name + "Value"] = tag.value
            else if @matchExpression(params.entityTypeName, rule.entityTypeLocator) && @matchExpression(fieldType.name, rule.propertyTypeLocator)
              matchScope = rule
    if matchType
      return matchType
    if matchScope
      return matchScope
    if matchTag
      return matchTag
    return defaultScope


  forEachPort: (port, entity, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    size = entity.entityType.fieldTypes.length
    for fieldType in entity.entityType.fieldTypes
      if fieldType.kind == "Property"
        widget = @getWidget port, {entityTypeName: entity.entityType.name, fieldType: fieldType, tags: fieldType.tags}
        fieldType.entity = entity.entityType
        propertyValue = entity[fieldType.name]
        property = {value: propertyValue, type: fieldType}
        widget.template.render property, (err, html) ->
          if err
            throw err
          result += html
          if (++current == size)
            callback(null, result)

class Filter.RelationshipFilter extends Filter.AbstractFilter
  constructor: () ->
    super("Relationship")

  getRule: (port, params) ->
    fieldType = params.fieldType
    found = null
    for rule in RulesCache
      if rule.portName == port
        if (rule.type == "RelationshipType" || rule.type == "Relationship") && fieldType.kind == "Relationship" 
          found = rule
    return found

  forEachPort: (port, entity, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    size = entity.entityType.fieldTypes.length
    for fieldType in entity.entityType.fieldTypes
      if fieldType.kind == "Relationship"
        widget = @getWidget port, {entityTypeName: entity.entityType.name, fieldType: fieldType}
        fieldType.entity = entity.entityType
        relationshipId = entity[fieldType.name]
        API.getEntity fieldType.targetType, relationshipId, (entity) =>
          relationship = {target: entity, type: fieldType}
          widget.template.render relationship, (err, html) ->
            if err
              throw err
            result += html
            if (++current == size)
              callback(null, result)

            
class Filter.FieldFilter extends Filter.AbstractFilter
  constructor: () ->
    super("Field")

  getRule: (port, params) ->
    fieldType = params.fieldType
    found = null
    for rule in RulesCache
      if rule.portName == port
        if (rule.type == "PropertyType" || rule.type == "Property") && fieldType.kind == "Property" 
          found = rule
        if (rule.type == "RelationshipType" || rule.type == "Relationship") && fieldType.kind == "Relationship" 
          found = rule
    return found

  forEachPort: (port, entity, callback) ->
    console.log @name + '.forEachPort: ' + port
    result = ""
    current = 0
    size = entity.entityType.fieldTypes.length
    for fieldType in entity.entityType.fieldTypes
      widget = @getWidget port, {entityTypeName: entity.entityType.name, fieldType: fieldType}
      fieldType.entity = entity.entityType
      if fieldType.kind == "Property"
        propertyValue = entity[fieldType.name]
        property = {value: propertyValue, type: fieldType}
        widget.template.render property, (err, html) ->
          if err
            throw err
          result += html
          if (++current == size)
            callback(null, result)
      else
        relationshipId = entity[fieldType.name]
        API.getEntity fieldType.targetType, relationshipId, (entity) =>
          relationship = {target: entity, type: fieldType}
          widget.template.render relationship, (err, html) ->
            if err
              throw err
            result += html
            if (++current == size)
              callback(null, result)

Filter.renderNewPageForEntityType = (port) ->
  filter = new Filter.EntityTypeFilter
  context = `this.ctx`
  filter.renderNewPage port, context
  
Filter.newPageForEntityType = (port, entityTypeName) ->
  filter = new Filter.EntityTypeFilter
  API.getEntityType entityTypeName, (entityType) =>
    filter.newPage port, entityType

Filter.forEachEntityType = (port, callback) =>
  filter = new Filter.EntityTypeFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.forEntityType = (port, callback) =>
  filter = new Filter.EntityTypeFilter
  context = `this.ctx`
  filter.forPort port, context, callback

Filter.renderNewPageForEntity = (port) ->
  filter = new Filter.EntityFilter
  context = `this.ctx`
  filter.renderNewPage port, context
	      
Filter.forEachEntity = (port, callback) ->
  filter = new Filter.EntityFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.newPageForEntity = (port, entityTypeName, entityId) ->
  filter = new Filter.EntityFilter
  API.getEntityType entityTypeName, (entityType) =>
    API.getEntity entityTypeName, entityId, (entity) =>
      filter.newPage port, entityType, entity

Filter.forEachPropertyType = (port, callback) ->
  filter = new Filter.PropertyTypeFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback
		
Filter.forEachRelationshipType = (port, callback) ->
  filter = new Filter.RelationshipTypeFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.forEachFieldType = (port, callback) ->
  filter = new Filter.FieldTypeFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.forEachProperty = (port, callback) ->
  filter = new Filter.PropertyFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.forEachRelationship = (port, callback) ->
  filter = new Filter.RelationshipFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.forEachField = (port, callback) ->
  filter = new Filter.FieldFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

Filter.forEachEnumValue = (port, callback) ->
  filter = new Filter.EnumTypeFilter
  context = `this.ctx`
  filter.forEachPort port, context, callback

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
	env.addFilter('forEachEnumValue', Filter.forEachEnumValue, true)
	env.addFilter('post', GUI.renderPostFilter, false)
	env.addFilter('put', GUI.renderPutFilter, false)
	env.addFilter('action', GUI.renderActionFilter, false)
	GUI.downloadAllRules () ->
		GUI.downloadAllWidgets () ->
			GUI.openApp()