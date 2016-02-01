class EntityUnorderedList extends EntitySetTemplate

	template: () ->
		"""<ul id='list_{{name}}'> 
		     {{renderEntities 'item' name}}
		   </ul>"""

return new EntityUnorderedList
