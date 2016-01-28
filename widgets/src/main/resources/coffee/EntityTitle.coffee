class EntityTitle extends EntitySetWidget

	render: (view) ->
		title = View.createEl("<h2>", "title", @entityType.name)
		title.append @entityType.name
		view.append title 
		
return new EntityTitle
