class EntityTitle extends EntitySetWidget

	render: (page, entityType) ->
		title = View.createEl("<h2>", "title", entityType.name)
		title.append entityType.name
		page.append title 
		
return new EntityTitle
