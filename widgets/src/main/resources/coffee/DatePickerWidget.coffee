class DatePickerWidget extends PropertyWidget

	render: (view) ->
		@textField = @createInputElement()
		if(@configuration)
			@textField.datepicker({altFormat: @configuration.format})
		else
			@textField.datepicker()
		
		if(@property)
			@textField.datepicker("setDate", new Date(@property));
		
		view.append @textField

	injectValue: (entity) ->
		entity[@propertyType.name] = new Date(@textField.val())

return new DatePickerWidget
