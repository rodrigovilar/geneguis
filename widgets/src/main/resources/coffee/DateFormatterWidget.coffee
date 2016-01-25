class DateFormatterWidget extends PropertyWidget

	render: (view) ->
		format = "yy-mm-dd" 
		if(@configuration)
			format = @configuration.format
		view.append $.datepicker.formatDate(format, new Date(@property))

return new DateFormatterWidget
