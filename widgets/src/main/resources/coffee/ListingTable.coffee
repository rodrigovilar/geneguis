class ListingTable extends EntitySetWidget

	render: (view) ->
		title = $("<h2>")
		title.append entityType.name
		view.append title 
		addButton = @createButtonForm "Add", entityType
		view.append addButton
		@table = $("<table>")
		view.append @table
		@buildTableHead(entityType.propertyTypes, @table);
		tbody = $("<tbody>");
		tbody.attr "id", "instances"
		table.append tbody
		DataManager.getEntities @entityType.resource, (entity) =>
			@buildTableLine(entity, entityType, tbody)

	buildTableHead: (properties, table) ->
		thead = $("<thead>");
		table.append thead
		trHead = $("<tr>");
		trHead.attr "id", "properties"
		thead.append trHead
		properties.forEach (property) ->
			thHead = $("<th>#{property.name}</th>")
			trHead.append thHead

	buildTableLine: (entity, entityType, tbody) ->
		trbody = $("<tr>")
		trbody.attr "id", "instance_" + entity.id
		tbody.append trbody
		entityType.propertyTypes.forEach (propertyType) =>
			td  = $("<td>");
			td.attr "id", "entity_" + entity.id + "_property_" + propertyType.name
			widget = RenderingEngine.getPropertyWidget 'property', entityType, propertyType 
			widget.propertyType = propertyType
			widget.property = entity[propertyType.name] 
			widget.render td
			trbody.append td
		
		editButton = @createButtonForm "Edit", entityType, entity
		td  = $("<td>");
		td.append editButton
		trbody.append td
		
		deleteButton = $("<button>")
		deleteButton.append "Delete"
		self = this
		deleteButton.click ->
			DataManager.deleteEntity(entityType.resource, entity.id)
			.done (data, textStatus, jqXHR) =>
				
				self.render(View.emptyPage(), entityType)
			.fail (jqXHR, textStatus, errorThrown) =>
				alert("Ocorreu o erro: " + status)
		td  = $("<td>");
		td.append deleteButton
		trbody.append td
		
	createButtonForm: (title, entityType, entity) =>
		formButton = $("<button>")
		formButton.append title
		formButton.click =>
			formWidget = RenderingEngine.getEntityWidget 'form', entityType
			formWidget.entityType = entityType
			if(entity)
				formWidget.entityID = entity.id
			RenderingEngine.pushWidget this
			formWidget.render View.emptyPage()
		formButton

return new ListingTable
