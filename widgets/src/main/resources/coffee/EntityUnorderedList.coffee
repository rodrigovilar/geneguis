class EntityUnorderedList extends EntitySetWidget

	render: (page, entityType) ->
		list = View.createEl("<ul>", "list", entityType.name)
		page.append list
		@renderItems list, entityType 
		
	renderItems: (list, entityType) ->
		itemWidget = RenderingEngine.getEntityWidget 'item', entityType 
		DataManager.getEntities entityType.name, (entity) =>
			itemWidget.render list, entityType, entity 
		
return new EntityUnorderedList
