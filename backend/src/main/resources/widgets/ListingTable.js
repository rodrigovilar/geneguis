(function() {
  var ListingTable,
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; },
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  ListingTable = (function(_super) {

    __extends(ListingTable, _super);

    function ListingTable() {
      this.createButtonForm = __bind(this.createButtonForm, this);
      return ListingTable.__super__.constructor.apply(this, arguments);
    }

    ListingTable.prototype.render = function(view) {
      var _this = this;
      return DataManager.getEntities(this.entityType.resource, function(entities) {
        return _this.drawTable(_this.entityType, entities, view);
      });
    };

    ListingTable.prototype.drawTable = function(entityType, entities, view) {
      var addButton, title;
      title = $("<h2>");
      title.append(entityType.name);
      view.append(title);
      addButton = this.createButtonForm("Add", entityType);
      view.append(addButton);
      this.table = $("<table>");
      view.append(this.table);
      this.buildTableHead(entityType.propertyTypes, this.table);
      return this.buildTableBody(entityType, entities, this.table);
    };

    ListingTable.prototype.buildTableHead = function(properties, table) {
      var thead, trHead;
      thead = $("<thead>");
      table.append(thead);
      trHead = $("<tr>");
      trHead.attr("id", "properties");
      thead.append(trHead);
      return properties.forEach(function(property) {
        var thHead;
        thHead = $("<th>" + property.name + "</th>");
        return trHead.append(thHead);
      });
    };

    ListingTable.prototype.buildTableBody = function(entityType, entities, table) {
      var tbody,
        _this = this;
      if (entities.length > 0) {
        tbody = $("<tbody>");
        tbody.attr("id", "instances");
        table.append(tbody);
        return entities.forEach(function(entity) {
          return _this.buildTableLine(entity, entityType, tbody);
        });
      } else {
        return table.append("There are not instances");
      }
    };

    ListingTable.prototype.buildTableLine = function(entity, entityType, tbody) {
      var deleteButton, editButton, self, td, trbody,
        _this = this;
      trbody = $("<tr>");
      trbody.attr("id", "instance_" + entity.id);
      tbody.append(trbody);
      entityType.propertyTypes.forEach(function(propertyType) {
        var td, widget;
        td = $("<td>");
        td.attr("id", "entity_" + entity.id + "_property_" + propertyType.name);
        widget = RenderingEngine.getPropertyWidget('property', entityType, propertyType);
        widget.propertyType = propertyType;
        widget.property = entity[propertyType.name];
        widget.render(td);
        return trbody.append(td);
      });
      editButton = this.createButtonForm("Edit", entityType, entity);
      td = $("<td>");
      td.append(editButton);
      trbody.append(td);
      deleteButton = $("<button>");
      deleteButton.append("Delete");
      self = this;
      deleteButton.click(function() {
        var _this = this;
        return DataManager.deleteEntity(entityType.resource, entity.id).done(function(data, textStatus, jqXHR) {
          return self.render(View.emptyPage(), entityType);
        }).fail(function(jqXHR, textStatus, errorThrown) {
          return alert("Ocorreu o erro: " + status);
        });
      });
      td = $("<td>");
      td.append(deleteButton);
      return trbody.append(td);
    };

    ListingTable.prototype.createButtonForm = function(title, entityType, entity) {
      var formButton,
        _this = this;
      formButton = $("<button>");
      formButton.append(title);
      formButton.click(function() {
        var formWidget;
        formWidget = RenderingEngine.getEntityWidget('form', entityType);
        formWidget.entityType = entityType;
        if (entity) {
          formWidget.entityID = entity.id;
        }
        RenderingEngine.pushWidget(_this);
        return formWidget.render(View.emptyPage());
      });
      return formButton;
    };

    return ListingTable;

  })(EntitySetWidget);

  return new ListingTable;

}).call(this);
