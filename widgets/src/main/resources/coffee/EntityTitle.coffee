class EntityTitle extends EntitySetTemplate

	template: () ->
		"""<h2 id='title_{{entityType.name}}'>
		     {{entityType.name}}
		   </h2>"""
		
return new EntityTitle
