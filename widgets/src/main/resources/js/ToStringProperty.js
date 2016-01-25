(function() {
  var ToStringProperty,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  ToStringProperty = (function(_super) {

    __extends(ToStringProperty, _super);

    function ToStringProperty() {
      return ToStringProperty.__super__.constructor.apply(this, arguments);
    }

    ToStringProperty.prototype.render = function(view) {
      return view.append(this.property);
    };

    return ToStringProperty;

  })(PropertyWidget);

  return new ToStringProperty;

}).call(this);
