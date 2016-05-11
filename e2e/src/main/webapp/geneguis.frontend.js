(function() {
  var _this = this;

  nunjucks.configure({
    autoescape: false
  });

  window.env = new nunjucks.Environment(null, {
    autoescape: false
  });

  Date.prototype.toISO8601 = function() {
    self = this;
    if (!isNaN(self)) {
      return self.toISOString().slice(0, 19) + 'Z';
    }
  };

  Date.prototype.toJSON = function() {
    return this.toISO8601();
  };

  window.HOST = 'http://localhost:8080/';

  window.RulesCache = [];

  window.WidgetCache = {};

  window.WidgetStack = [];

  window.GUI = {};

  window.API = {};

  window.Filter = {};

  GUI.downloadAllRules = function(callback) {
    var _this = this;
    return $.getJSON(HOST + 'rules', function(rules) {
      rules.forEach(function(rule) {
        return RulesCache.push(rule);
      });
      return callback();
    });
  };

  GUI.getWidgetByRule = function(rule, callback) {
    var widget;
    widget = WidgetCache[rule.widgetName + rule.widgetVersion];
    widget.configuration = $.parseJSON(rule.configuration);
    console.log('getWidgetByRule: ' + rule.portName + '->' + widget.name);
    return callback(widget);
  };

  GUI.getEntityTypeWidget = function(portName, entityTypeName, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        return found = rule;
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getEntityWidget = function(portName, entityTypeName, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        return found = rule;
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getPropertyTypeWidget = function(portName, entityTypeName, fieldType, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        if ((rule.type === "PropertyType" || rule.type === "Property" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Property") {
          return found = rule;
        }
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getRelationshipTypeWidget = function(portName, entityTypeName, fieldType, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        if ((rule.type === "RelationshipType" || rule.type === "Relationship" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Relationship") {
          return found = rule;
        }
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getFieldTypeWidget = function(portName, entityTypeName, fieldType, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        if ((rule.type === "PropertyType" || rule.type === "Property" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Property") {
          found = rule;
        }
        if ((rule.type === "RelationshipType" || rule.type === "Relationship" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Relationship") {
          return found = rule;
        }
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getFieldWidget = function(portName, entityTypeName, fieldType, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        if ((rule.type === "PropertyType" || rule.type === "Property") && fieldType.kind === "Property") {
          found = rule;
        }
        if ((rule.type === "RelationshipType" || rule.type === "Relationship") && fieldType.kind === "Relationship") {
          return found = rule;
        }
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getPropertyWidget = function(portName, entityTypeName, fieldType, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        if ((rule.type === "PropertyType" || rule.type === "Property") && fieldType.kind === "Property") {
          return found = rule;
        }
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getRelationshipWidget = function(portName, entityTypeName, fieldType, callback) {
    var found,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        if ((rule.type === "RelationshipType" || rule.type === "Relationship") && fieldType.kind === "Relationship") {
          return found = rule;
        }
      }
    });
    if (found) {
      return GUI.getWidgetByRule(found, callback);
    } else {
      return console.log('No rules found for port:' + portName);
    }
  };

  GUI.getWidget = function(portName, entityTypeName, propertyType, callback) {
    var found, widget,
      _this = this;
    found = null;
    RulesCache.forEach(function(rule) {
      if (rule.portName === portName) {
        return found = rule;
      }
    });
    if (found) {
      widget = WidgetCache[found.widgetName + found.widgetVersion];
      widget.configuration = $.parseJSON(found.configuration);
      console.log('getWidget: ' + portName + '->' + widget.name);
      return callback(widget);
    }
  };

  GUI.downloadAllWidgets = function(callback) {
    var _this = this;
    return $.getJSON(HOST + 'widgets', function(widgetSpecs) {
      widgetSpecs.forEach(function(widgetSpec) {
        widgetSpec.template = new nunjucks.Template(widgetSpec.code, window.env, null, true);
        return WidgetCache[widgetSpec.name + widgetSpec.version] = widgetSpec;
      });
      return callback();
    });
  };

  GUI.newPage = function(portName, entityTypeName, callback) {
    var body, page;
    body = $("body");
    body.empty();
    page = $('<div>');
    page.attr("id", "page_view");
    body.append(page);
    return GUI.getWidget(portName, entityTypeName, null, function(widget) {
      page.rootWidget = widget;
      page.rootWidget.context = {
        name: entityTypeName
      };
      WidgetStack.push(page);
      return callback(page);
    });
  };

  GUI.back = function() {
    WidgetStack.pop();
    return GUI.refresh();
  };

  GUI.refresh = function() {
    var body, page, widget;
    body = $("body");
    body.empty();
    page = $('<div>');
    page.attr("id", "page_view");
    body.append(page);
    widget = WidgetStack[WidgetStack.length - 1].rootWidget;
    return widget.template.render(widget.context, function(err, html) {
      if (err) {
        throw err;
      }
      return page.html(html);
    });
  };

  API.loadData = function(url, callback) {
    var _this = this;
    return $.getJSON(HOST + url, function(json) {
      return callback(json);
    });
  };

  API.getEntitiesTypes = function(callback) {
    return API.loadData('entities', callback);
  };

  API.getEntityType = function(entityId, callback) {
    return API.loadData('entities/' + entityId, callback);
  };

  API.getEntities = function(entityTypeResource, callback) {
    var _this = this;
    return API.loadData('api/' + entityTypeResource, function(entities) {
      return callback(entities);
    });
  };

  API.getEntity = function(entityTypeResource, entityID, callback) {
    return API.loadData('api/' + entityTypeResource + '/' + entityID, callback);
  };

  API.createEntity = function(entityTypeResource, entity, callback) {
    return $.ajax({
      url: HOST + "api/" + entityTypeResource,
      type: "POST",
      data: JSON.stringify(entity),
      contentType: "application/json; charset=utf-8"
    }).done(function() {
      return callback();
    });
  };

  API.updateEntity = function(entityTypeResource, entity, callback) {
    return $.ajax({
      url: HOST + "api/" + entityTypeResource + "/" + entity.id,
      type: "PUT",
      data: JSON.stringify(entity),
      contentType: "application/json; charset=utf-8"
    }).done(function() {
      return callback();
    });
  };

  API.deleteEntity = function(entityTypeResource, entityID, success, error) {
    return $.ajax({
      url: HOST + "api/" + entityTypeResource + "/" + entityID,
      type: "DELETE"
    });
  };

  GUI.postAndBack = function(entityTypeName) {
    var entity, formData,
      _this = this;
    console.log('postAndBack: ' + entityTypeName);
    formData = $("#create_" + entityTypeName).serializeArray();
    entity = {};
    formData.forEach(function(field) {
      return entity[field.name.substring(18)] = field.value === "" ? null : field.value;
    });
    return API.createEntity(entityTypeName, entity, function() {
      console.log('POST OK: ' + entityTypeName + ' ' + JSON.stringify(entity));
      return GUI.back();
    });
  };

  GUI.putAndBack = function(entityTypeName) {
    var entity, formData,
      _this = this;
    console.log('putAndBack: ' + entityTypeName);
    formData = $("#edit_" + entityTypeName).serializeArray();
    entity = {};
    formData.forEach(function(field) {
      return entity[field.name.substring(16)] = field.value === "" ? null : field.value;
    });
    return API.updateEntity(entityTypeName, entity, function() {
      console.log('PUT OK: ' + entityTypeName + ' ' + JSON.stringify(entity));
      return GUI.back();
    });
  };

  GUI.remove = function(entityTypeName, entityID) {
    return API.deleteEntity(entityTypeName, entityID);
  };

  GUI.newPageByPort = function(portName, entityTypeName, entityId) {
    console.log('newPageByPort: ' + portName);
    return GUI.newPage(portName, entityTypeName, function(view) {
      var _this = this;
      if (view.rootWidget.type === "EntityTypeSet") {
        return API.getEntitiesTypes(function(entitiesTypes) {
          view.rootWidget.context = entitiesTypes;
          return view.rootWidget.template.render(entitiesTypes, function(err, html) {
            if (err) {
              throw err;
            }
            return view.html(html);
          });
        });
      }
    });
  };

  GUI.openApp = function() {
    return GUI.newPageByPort('root');
  };

  GUI.renderPortFilter = function(port, callback) {
    var _this = this;
    console.log('renderPortFilter: ' + port);
    return GUI.getWidget(port, this.ctx.name, null, function(widget) {
      if (widget.type === "EntityTypeSet") {
        return API.getEntitiesTypes(function(entitiesTypes) {
          return widget.template.render(entitiesTypes, function(err, html) {
            if (err) {
              throw err;
            }
            return callback(err, html);
          });
        });
      }
    });
  };

  GUI.renderNewPageFilter = function(port) {
    var entityTypeName;
    entityTypeName = this.ctx.entityType ? this.ctx.entityType.name : this.ctx.name;
    return "onClick=\"GUI.newPageByPort('" + port + "','" + entityTypeName + "'," + this.ctx.id + ")\"";
  };

  GUI.renderPostFilter = function(port) {
    return "onClick=\"GUI.postAndBack('" + this.ctx.name + "');return false;\"";
  };

  GUI.renderPutFilter = function(port) {
    return "onClick=\"GUI.putAndBack('" + this.ctx.entityType.name + "');return false;\"";
  };

  GUI.renderActionFilter = function(action) {
    return "onClick=\"GUI." + action + "('" + this.ctx.entityType.name + "', " + this.ctx.id + ");GUI.refresh();\"";
  };

  Filter.forEachEntityType = function(port, callback) {
    var _this = this;
    console.log('forEachEntityType: ' + port);
    return GUI.getEntityTypeWidget(port, this.ctx.name, function(widget) {
      var entityType, i, j, msg, result;
      if (widget.type !== "EntityType") {
        msg = 'forEachEntityType: ' + widget.name + ' is not EntityType';
        console.error(msg);
        return callback(msg, null);
      } else {
        i = 0;
        j = 0;
        result = "";
        entityType = _this.ctx[i];
        while (entityType) {
          widget.template.render(entityType, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            j++;
            if (i === j) {
              return callback(null, result);
            }
          });
          entityType = _this.ctx[++i];
        }
      }
    });
  };

  Filter.renderNewPageForEntityType = function(portName) {
    var entityTypeName;
    entityTypeName = this.ctx.name;
    return "onClick=\"Filter.newPageForEntityType('" + portName + "','" + entityTypeName + "')\"";
  };

  Filter.newPageForEntityType = function(portName, entityTypeName) {
    console.log('newPageForEntityType: ' + portName);
    return GUI.newPage(portName, entityTypeName, function(view) {
      var _this = this;
      return API.getEntityType(entityTypeName, function(entityType) {
        view.rootWidget.context = entityType;
        return view.rootWidget.template.render(entityType, function(err, html) {
          if (err) {
            throw err;
          }
          return view.html(html);
        });
      });
    });
  };

  Filter.forEntityType = function(port, callback) {
    var _this = this;
    console.log('forEntityType: ' + port);
    return GUI.getEntityWidget(port, this.ctx.name, function(widget) {
      var msg;
      if (widget.type !== "EntityType") {
        msg = 'forEntityType: ' + widget.name + ' is not EntityType';
        console.error(msg);
        return callback(msg, null);
      } else {
        return API.getEntityType(_this.ctx.name, function(entityType) {
          return widget.template.render(entityType, function(err, html) {
            if (err) {
              throw err;
            }
            return callback(err, html);
          });
        });
      }
    });
  };

  Filter.forEachEntity = function(port, callback) {
    var entityTypeName,
      _this = this;
    console.log('forEachEntity: ' + port);
    entityTypeName = this.ctx.name;
    return GUI.getEntityWidget(port, entityTypeName, function(widget) {
      return API.getEntities(entityTypeName, function(entities) {
        var i, len, result;
        result = "";
        i = 1;
        len = entities.length;
        return entities.forEach(function(entity) {
          entity.entityType = _this.ctx;
          return widget.template.render(entity, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (i === len) {
              callback(err, result);
            }
            return i++;
          });
        });
      });
    });
  };

  Filter.forEachPropertyType = function(port, callback) {
    var entityType, i, len, result;
    console.log('forEachPropertyType: ' + port);
    entityType = this.ctx;
    result = "";
    i = 1;
    len = entityType.fieldTypes.length;
    return entityType.fieldTypes.forEach(function(fieldType) {
      if (fieldType.kind === "Property") {
        return GUI.getPropertyTypeWidget(port, entityType.name, fieldType, function(widget) {
          fieldType.entity = entityType;
          return widget.template.render(fieldType, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (i === len) {
              callback(null, result);
            }
            return i++;
          });
        });
      }
    });
  };

  Filter.forEachRelationshipType = function(port, callback) {
    var entityType, i, len, result;
    console.log('forEachRelationshipType: ' + port);
    entityType = this.ctx;
    result = "";
    i = 1;
    len = entityType.fieldTypes.length;
    return entityType.fieldTypes.forEach(function(fieldType) {
      if (fieldType.kind === "Relationship") {
        return GUI.getRelationshipTypeWidget(port, entityType.name, fieldType, function(widget) {
          fieldType.entity = entityType;
          return widget.template.render(fieldType, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (i === len) {
              callback(null, result);
            }
            return i++;
          });
        });
      }
    });
  };

  Filter.forEachFieldType = function(port, callback) {
    var entityType, i, len, result;
    console.log('forEachFieldType: ' + port);
    entityType = this.ctx;
    result = "";
    i = 1;
    len = entityType.fieldTypes.length;
    return entityType.fieldTypes.forEach(function(fieldType) {
      return GUI.getFieldTypeWidget(port, entityType.name, fieldType, function(widget) {
        fieldType.entity = entityType;
        return widget.template.render(fieldType, function(err, html) {
          if (err) {
            throw err;
          }
          result += html;
          if (i === len) {
            callback(null, result);
          }
          return i++;
        });
      });
    });
  };

  Filter.forEachProperty = function(port, callback) {
    var entity, entityType, i, len, result;
    console.log('forEachProperty: ' + port);
    entity = this.ctx;
    entityType = this.ctx.entityType ? this.ctx.entityType : this.ctx.type;
    result = "";
    i = 1;
    len = entityType.fieldTypes.length;
    return entityType.fieldTypes.forEach(function(fieldType) {
      if (fieldType.kind === "Property") {
        return GUI.getPropertyWidget(port, entityType.name, fieldType, function(widget) {
          var property, propertyValue;
          fieldType.entity = entityType;
          propertyValue = entity[fieldType.name];
          property = {
            value: propertyValue,
            type: fieldType
          };
          return widget.template.render(property, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (i === len) {
              callback(null, result);
            }
            return i++;
          });
        });
      }
    });
  };

  Filter.forEachRelationship = function(port, callback) {
    var entity, entityType, i, len, result;
    console.log('forEachRelationship: ' + port);
    entity = this.ctx;
    entityType = this.ctx.entityType ? this.ctx.entityType : this.ctx.type;
    result = "";
    i = 1;
    len = entityType.fieldTypes.length;
    return entityType.fieldTypes.forEach(function(fieldType) {
      if (fieldType.kind === "Relationship") {
        return GUI.getRelationshipWidget(port, entityType.name, fieldType, function(widget) {
          var relationshipId,
            _this = this;
          fieldType.entity = entityType;
          relationshipId = entity[fieldType.name];
          return API.getEntity(fieldType.targetType, relationshipId, function(entity) {
            var relationship;
            relationship = {
              target: entity,
              type: fieldType
            };
            return widget.template.render(relationship, function(err, html) {
              if (err) {
                throw err;
              }
              result += html;
              if (i === len) {
                callback(null, result);
              }
              return i++;
            });
          });
        });
      }
    });
  };

  Filter.forEachField = function(port, callback) {
    var entity, entityType, i, len, result;
    console.log('forEachField: ' + port);
    entity = this.ctx;
    entityType = this.ctx.entityType ? this.ctx.entityType : this.ctx.type;
    result = "";
    i = 1;
    len = entityType.fieldTypes.length;
    return entityType.fieldTypes.forEach(function(fieldType) {
      return GUI.getFieldWidget(port, entityType.name, fieldType, function(widget) {
        var field, propertyValue, relationshipId,
          _this = this;
        fieldType.entity = entityType;
        field = null;
        if (fieldType.kind === "Property") {
          propertyValue = entity[fieldType.name];
          field = {
            value: propertyValue,
            type: fieldType
          };
          return widget.template.render(field, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (i === len) {
              callback(null, result);
            }
            return i++;
          });
        } else {
          relationshipId = entity[fieldType.name];
          return API.getEntity(fieldType.targetType, relationshipId, function(entity) {
            field = {
              target: entity,
              type: fieldType
            };
            return widget.template.render(field, function(err, html) {
              if (err) {
                throw err;
              }
              result += html;
              if (i === len) {
                callback(null, result);
              }
              return i++;
            });
          });
        }
      });
    });
  };

  Filter.renderNewPageForEntity = function(portName) {
    var entityTypeName;
    entityTypeName = this.ctx.entityType.name;
    return "onClick=\"Filter.newPageForEntity('" + portName + "','" + entityTypeName + "'," + this.ctx.id + ")\"";
  };

  Filter.newPageForEntity = function(portName, entityTypeName, entityId) {
    console.log('newPageForEntity: ' + portName);
    return GUI.newPage(portName, entityTypeName, function(view) {
      var _this = this;
      return API.getEntityType(entityTypeName, function(entityType) {
        return API.getEntity(entityTypeName, entityId, function(entity) {
          entity.entityType = entityType;
          view.rootWidget.context = entity;
          return view.rootWidget.template.render(entity, function(err, html) {
            if (err) {
              throw err;
            }
            return view.html(html);
          });
        });
      });
    });
  };

  $(function() {
    env.addFilter('forEachEntityType', Filter.forEachEntityType, true);
    env.addFilter('newPageForEntityType', Filter.renderNewPageForEntityType, false);
    env.addFilter('forEntityType', Filter.forEntityType, true);
    env.addFilter('forEachEntity', Filter.forEachEntity, true);
    env.addFilter('newPageForEntity', Filter.renderNewPageForEntity, false);
    env.addFilter('forEachPropertyType', Filter.forEachPropertyType, true);
    env.addFilter('forEachProperty', Filter.forEachProperty, true);
    env.addFilter('forEachFieldType', Filter.forEachFieldType, true);
    env.addFilter('forEachField', Filter.forEachField, true);
    env.addFilter('post', GUI.renderPostFilter, false);
    env.addFilter('put', GUI.renderPutFilter, false);
    env.addFilter('action', GUI.renderActionFilter, false);
    return GUI.downloadAllRules(function() {
      return GUI.downloadAllWidgets(function() {
        return GUI.openApp();
      });
    });
  });

}).call(this);
