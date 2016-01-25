class NumberFormatter extends PropertyWidget

	render: (view) ->
		if(@configuration.editable)
			@textField = $("<input>")
			@textField.val(@property)
			@textField.mask(@configuration.format)
			view.append @textField
		else
			view.append @property
			view.mask(@configuration.format)
			
	injectValue: (entity) ->
		entity[@propertyType.name] = @textField.cleanVal()
			

return new NumberFormatter
