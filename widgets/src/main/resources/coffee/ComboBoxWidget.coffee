class ComboBoxWidget extends RelationshipWidget

	render: (view) ->
		@selectField = $("<select>")
		view.append @selectField
		if (@relationship)
			relationshipIds = [@relationship.id]
		if (@configuration)
			@populateSelectField @selectField, @relationshipType.targetType.resource, @configuration.propertyKey, relationshipIds
		else
			@populateSelectField @selectField, @relationshipType.targetType.resource, null, relationships
	
	injectValue: (entity) ->
		entity[@relationshipType.name] = {id: parseInt(@selectField.val())}

return new ComboBoxWidget