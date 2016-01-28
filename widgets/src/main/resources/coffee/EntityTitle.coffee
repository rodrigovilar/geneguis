class EntityTitle extends EntitySetWidget

	render: (view) ->
		title = $("<h2>")
		title.attr "id", "title_" + @entityType.name
		title.append @entityType.name
		view.append title 
		
return new EntityTitle
