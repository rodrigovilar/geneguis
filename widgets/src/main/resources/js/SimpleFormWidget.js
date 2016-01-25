(function() {
  var SimpleFormWidget,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  SimpleFormWidget = (function(_super) {

    __extends(SimpleFormWidget, _super);

    function SimpleFormWidget() {
      return SimpleFormWidget.__super__.constructor.apply(this, arguments);
    }

    SimpleFormWidget.prototype.render = function(view) {
      var self,
        _this = this;
      self = this;
      if (this.entityID) {
        return DataManager.getEntity(this.entityType.resource, this.entityID, function(entity) {
          return self.draw(view, self.entityType, entity);
        });
      } else {
        return self.draw(view, this.entityType);
      }
    };

    SimpleFormWidget.prototype.draw = function(view, entityType, entity) {
      var self, table, title;
      title = $("<h2>");
      title.append(entityType.name);
      view.append(title);
      table = $("<table>");
      view.append(table);
      this.widgets = [];
      self = this;
      entityType.propertyTypes.forEach(function(propertyType) {
        if (propertyType.name !== "id") {
          return self.renderTableRow(view, entityType, entity, propertyType, false);
        }
      });
      entityType.relationshipTypes.forEach(function(relationshipType) {
        return self.renderTableRow(view, entityType, entity, relationshipType, true);
      });
      return this.renderSubmitButton(view, entityType.resource, entity);
    };

    SimpleFormWidget.prototype.renderTableRow = function(view, entityType, entity, metadata, isRelationship) {
      var td, tr, widget;
      tr = $("<tr>");
      td = $("<td>");
      td.append(metadata.name);
      tr.append(td);
      td = $("<td>");
      if (isRelationship) {
        widget = RenderingEngine.getRelationshipWidget('fieldRelation', entityType, metadata);
        widget.relationshipType = metadata;
        if (entity) {
          widget.relationship = entity[metadata.name];
        }
      } else {
        widget = RenderingEngine.getPropertyWidget('field', entityType, metadata);
        widget.propertyType = metadata;
        if (entity) {
          widget.property = entity[metadata.name];
        }
      }
      widget.render(td);
      this.widgets.push(widget);
      tr.append(td);
      return view.append(tr);
    };

    SimpleFormWidget.prototype.getEntityValuesFromInput = function() {
      var entity;
      entity = {};
      this.widgets.forEach(function(widget) {
        return widget.injectValue(entity);
      });
      return entity;
    };

    SimpleFormWidget.prototype.renderSubmitButton = function(view, resource, entity) {
      var self, submitButton;
      submitButton = $("<button>");
      self = this;
      if (entity) {
        submitButton.append("Update");
      } else {
        submitButton.append("Create");
      }
      submitButton.click(function() {
        var newEntityValues, request,
          _this = this;
        newEntityValues = self.getEntityValuesFromInput();
        request = null;
        if (entity) {
          newEntityValues["id"] = entity.id;
          request = DataManager.updateEntity(resource, newEntityValues);
        } else {
          request = DataManager.createEntity(resource, newEntityValues);
        }
        return request.done(function() {
          return RenderingEngine.popAndRender(View.emptyPage());
        }).fail(function() {
          return alert("Error");
        });
      });
      return view.append(submitButton);
    };

    return SimpleFormWidget;

  })(EntityWidget);

  return new SimpleFormWidget;

}).call(this);
