class SimpleFormWidget extends EntityWidget

	render: (view) ->
		self = this
		if(@entityID)
			DataManager.getEntity @entityType.resource, @entityID, (entity) =>
				self.draw(view, self.entityType, entity)
		else
			self.draw(view, @entityType)

	draw: (view, entityType, entity) ->
		title = $("<h2>")
		title.append entityType.name
		view.append title
		table = $("<table>")
		view.append table
		@widgets = []
		self = this
		entityType.propertyTypes.forEach (propertyType) ->
			if(propertyType.name != "id")
				self.renderTableRow view, entityType, entity, propertyType, false
		entityType.relationshipTypes.forEach (relationshipType) ->
			self.renderTableRow view, entityType, entity, relationshipType, true
		@renderSubmitButton(view, entityType.resource, entity)

	renderTableRow: (view, entityType, entity, metadata, isRelationship) ->
		tr = $("<tr>")
		
		td  = $("<td>");
		td.append metadata.name
		tr.append td
		
		td  = $("<td>");
		
		if(isRelationship)
			widget = RenderingEngine.getRelationshipWidget 'fieldRelation', entityType, metadata
			widget.relationshipType = metadata
			if(entity)
				widget.relationship = entity[metadata.name]
		else
			widget = RenderingEngine.getPropertyWidget 'field', entityType, metadata
			widget.propertyType = metadata
			if(entity)
				widget.property = entity[metadata.name]
		
		widget.render td
		@widgets.push(widget)
		tr.append td
		
		view.append tr

	getEntityValuesFromInput: () ->
		entity = {}
		@widgets.forEach (widget) ->
			widget.injectValue(entity)
		entity

	renderSubmitButton: (view, resource, entity) ->
		submitButton = $("<button>")
		self = this
		if(entity)
			submitButton.append "Update"
		else
			submitButton.append "Create"
		submitButton.click ->
			newEntityValues = self.getEntityValuesFromInput()
			request = null
			if(entity)
				newEntityValues["id"] = entity.id
				request = DataManager.updateEntity(resource, newEntityValues)
			else
				request = DataManager.createEntity(resource, newEntityValues)
			request.done =>
				RenderingEngine.popAndRender View.emptyPage()
			.fail =>
				alert("Error")
		view.append submitButton

return new SimpleFormWidget
