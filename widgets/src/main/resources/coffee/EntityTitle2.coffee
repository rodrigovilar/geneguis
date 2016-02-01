class EntityTitle2 extends EntitySetTemplate

	template: () ->
		"""<h2 id='title_{{name}}'>
		     {{name}}
		   </h2>
		   {{renderEntitySet 'list' name}}
		"""
		
return new EntityTitle2
