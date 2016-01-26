(function() {
  var ComboBoxWidget,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  ComboBoxWidget = (function(_super) {

    __extends(ComboBoxWidget, _super);

    function ComboBoxWidget() {
      return ComboBoxWidget.__super__.constructor.apply(this, arguments);
    }

    ComboBoxWidget.prototype.render = function(view) {
      var relationshipIds;
      this.selectField = $("<select>");
      view.append(this.selectField);
      if (this.relationship) {
        relationshipIds = [this.relationship.id];
      }
      if (this.configuration) {
        return this.populateSelectField(this.selectField, this.relationshipType.targetType.resource, this.configuration.propertyKey, relationshipIds);
      } else {
        return this.populateSelectField(this.selectField, this.relationshipType.targetType.resource, null, relationships);
      }
    };

    ComboBoxWidget.prototype.injectValue = function(entity) {
      return entity[this.relationshipType.name] = {
        id: parseInt(this.selectField.val())
      };
    };

    return ComboBoxWidget;

  })(RelationshipWidget);

  return new ComboBoxWidget;

}).call(this);
