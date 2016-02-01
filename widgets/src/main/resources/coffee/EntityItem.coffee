class EntityItem extends EntityTemplate

	template: () ->
		"""<li id='li_{{entity.id}}'>{{entity.id}}</li>"""

return new EntityItem
