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
      var self, submitButton, table, title;
      title = $("<h2>");
      title.append(entityType.name);
      view.append(title);
      table = $("<table>");
      view.append(table);
      entityType.propertiesType.forEach(function(property) {
        var td, textField, tr;
        if (property.name !== "id") {
          tr = $("<tr>");
          td = $("<td>");
          td.append(property.name);
          tr.append(td);
          td = $("<td>");
          textField = $("<input>");
          textField.attr("id", entityType.resource + "_" + property.name);
          if (entity) {
            textField.val(entity[property.name]);
          }
          td.append(textField);
          tr.append(td);
          return view.append(tr);
        }
      });
      submitButton = $("<button>");
      self = this;
      if (entity) {
        submitButton.append("Update");
        submitButton.click(function() {
          var newEntityValues,
            _this = this;
          newEntityValues = self.getEntityValuesFromInput(entityType);
          newEntityValues["id"] = entity.id;
          return DataManager.updateEntity(entityType.resource, newEntityValues).done(function() {
            return RenderingEngine.popAndRender(View.emptyPage());
          }).fail(function() {
            return alert("Error");
          });
        });
      } else {
        submitButton.append("Create");
        submitButton.click(function() {
          var newEntityValues,
            _this = this;
          newEntityValues = self.getEntityValuesFromInput(entityType);
          return DataManager.createEntity(entityType.resource, newEntityValues).done(function() {
            return RenderingEngine.popAndRender(View.emptyPage());
          }).fail(function() {
            return alert("Error");
          });
        });
      }
      return view.append(submitButton);
    };

    SimpleFormWidget.prototype.getEntityValuesFromInput = function(entityType) {
      var entity;
      entity = {};
      entityType.propertiesType.forEach(function(property) {
        var value;
        if (property.name !== "id") {
          value = $("#" + entityType.resource + "_" + property.name).val();
          if (value && property.type === "int") {
            return value = parseInt(value);
          } else if (value && property.type === "real") {
            return value = parseFloat(value);
          } else if (value && property.type === "date") {
            return value = new Date(value);
          } else if (value) {
            return entity[property.name] = value;
          }
        }
      });
      return entity;
    };

    return SimpleFormWidget;

  })(EntityWidget);

  return new SimpleFormWidget;

}).call(this);