(function() {
  var SimpleTextFieldProperty,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  SimpleTextFieldProperty = (function(_super) {

    __extends(SimpleTextFieldProperty, _super);

    function SimpleTextFieldProperty() {
      return SimpleTextFieldProperty.__super__.constructor.apply(this, arguments);
    }

    SimpleTextFieldProperty.prototype.render = function(view) {
      var value;
      this.textField = this.createInputElement();
      if (this.property) {
        value = this.property;
        if (this.propertyType.type === "date") {
          value = $.datepicker.formatDate("yy-mm-dd", new Date(this.property));
        }
        this.textField.val(value);
      }
      return view.append(this.textField);
    };

    SimpleTextFieldProperty.prototype.injectValue = function(entity) {
      var value;
      value = this.textField.val();
      if (this.propertyType.type === "integer") {
        value = parseInt(value);
      }
      if (this.propertyType.type === "real") {
        value = parseFloat(value);
      }
      if (this.propertyType.type === "date") {
        value = new Date(value);
      }
      return entity[this.propertyType.name] = value;
    };

    return SimpleTextFieldProperty;

  })(PropertyWidget);

  return new SimpleTextFieldProperty;

}).call(this);
