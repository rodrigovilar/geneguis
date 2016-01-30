(function() {
  var DatePickerWidget,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  DatePickerWidget = (function(_super) {

    __extends(DatePickerWidget, _super);

    function DatePickerWidget() {
      return DatePickerWidget.__super__.constructor.apply(this, arguments);
    }

    DatePickerWidget.prototype.render = function(view) {
      this.textField = this.createInputElement();
      if (this.configuration) {
        this.textField.datepicker({
          altFormat: this.configuration.format
        });
      } else {
        this.textField.datepicker();
      }
      if (this.property) {
        this.textField.datepicker("setDate", new Date(this.property));
      }
      return view.append(this.textField);
    };

    DatePickerWidget.prototype.injectValue = function(entity) {
      return entity[this.propertyType.name] = new Date(this.textField.val());
    };

    return DatePickerWidget;

  })(PropertyWidget);

  return new DatePickerWidget;

}).call(this);
