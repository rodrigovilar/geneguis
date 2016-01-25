class MultiSelectBoxWidget extends RelationshipWidget

	render: (view) ->
		@multiSelectField = $("<select multiple>")
		view.append @multiSelectField
		relationshipsIds = []
		if(@relationship)
			@relationship.forEach (entity) =>
				relationshipsIds.push entity.id
		if (@configuration)
			@populateSelectField @multiSelectField, @relationshipType.targetType.resource, @configuration.propertyKey, relationshipsIds
		else
			@populateSelectField @multiSelectField, @relationshipType.targetType.resource, null, relationshipsIds
	
	injectValue: (entity) ->
		value = []
		if(@multiSelectField.val())
			@multiSelectField.val().forEach (selected) =>
				value.push {id: parseInt(selected)}
		entity[@relationshipType.name] = value 

return new MultiSelectBoxWidget
