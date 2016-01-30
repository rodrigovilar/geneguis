class EntityItem extends EntityWidget

	render: (list, entityType, entity) ->
		item = View.createEl("<li>#{entity}</li>", "li", entity.id)
		list.append item
		
return new EntityItem
