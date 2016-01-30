class EntityTitle2 extends EntitySetWidget

	template: () ->
		"""<h2 id='title_{{entityType.name}}'>
		     {{entityType.name}}
		   </h2>
		   {{renderEntitySet list entityType}}
		"""

	render: (page, entityType) ->
		@renderTitle page, entityType
		@renderList page, entityType
		
	renderTitle: (page, entityType) =>
		title = View.createEl("<h2>", "title", entityType.name) 
		title.append entityType.name
		page.append title 
		
	renderList: (page, entityType) =>
		listWidget = RenderingEngine.getEntitySetWidget 'list', entityType
		listWidget.render page, entityType
		
return new EntityTitle2
