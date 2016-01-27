(function() {
  var MultiSelectBoxWidget,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  MultiSelectBoxWidget = (function(_super) {

    __extends(MultiSelectBoxWidget, _super);

    function MultiSelectBoxWidget() {
      return MultiSelectBoxWidget.__super__.constructor.apply(this, arguments);
    }

    MultiSelectBoxWidget.prototype.render = function(view) {
      var relationshipsIds,
        _this = this;
      this.multiSelectField = $("<select multiple>");
      view.append(this.multiSelectField);
      relationshipsIds = [];
      if (this.relationship) {
        this.relationship.forEach(function(entity) {
          return relationshipsIds.push(entity.id);
        });
      }
      if (this.configuration) {
        return this.populateSelectField(this.multiSelectField, this.relationshipType.targetType.resource, this.configuration.propertyKey, relationshipsIds);
      } else {
        return this.populateSelectField(this.multiSelectField, this.relationshipType.targetType.resource, null, relationshipsIds);
      }
    };

    MultiSelectBoxWidget.prototype.injectValue = function(entity) {
      var value,
        _this = this;
      value = [];
      if (this.multiSelectField.val()) {
        this.multiSelectField.val().forEach(function(selected) {
          return value.push({
            id: parseInt(selected)
          });
        });
      }
      return entity[this.relationshipType.name] = value;
    };

    return MultiSelectBoxWidget;

  })(RelationshipWidget);

  return new MultiSelectBoxWidget;

}).call(this);
