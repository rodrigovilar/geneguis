class SimpleTextFieldProperty extends PropertyWidget

	render: (view) ->
		@textField = @createInputElement()
		
		if(@property)
			value = @property
			if(@propertyType.type == "date")
				value = $.datepicker.formatDate("yy-mm-dd", new Date(@property))
			@textField.val(value)
		
		view.append @textField

	injectValue: (entity) ->
		value = @textField.val()
		if(@propertyType.type == "integer")
			value = parseInt(value)
		if(@propertyType.type == "real")
			value = parseFloat(value)
		if(@propertyType.type == "date")
			value = new Date(value)
		entity[@propertyType.name] = value

return new SimpleTextFieldProperty
