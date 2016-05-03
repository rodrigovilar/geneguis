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

  GUI.downloadAllRules = function(callback) {
    var _this = this;
    return $.getJSON(HOST + 'rules', function(rules) {
      rules.forEach(function(rule) {
        return RulesCache.push(rule);
      });
      return callback();
    });
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
        API.getEntitiesTypes(function(entitiesTypes) {
          view.rootWidget.context = entitiesTypes;
          return view.rootWidget.template.render(entitiesTypes, function(err, html) {
            return view.html(html);
          });
        });
      }
      if (view.rootWidget.type === "EntityType") {
        API.getEntityType(entityTypeName, function(entityType) {
          view.rootWidget.context = entityType;
          return view.rootWidget.template.render(entityType, function(err, html) {
            return view.html(html);
          });
        });
      }
      if (view.rootWidget.type === "Entity") {
        return API.getEntityType(entityTypeName, function(entityType) {
          return API.getEntity(entityTypeName, entityId, function(entity) {
            entity.entityType = entityType;
            view.rootWidget.context = entity;
            return view.rootWidget.template.render(entity, function(err, html) {
              return view.html(html);
            });
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
      var entity, entityType, i, j, len, result;
      if (widget.type === "EntityTypeSet") {
        API.getEntitiesTypes(function(entitiesTypes) {
          return widget.template.render(entitiesTypes, function(err, html) {
            return callback(null, html);
          });
        });
      }
      if (widget.type === "EntityType") {
        if (_this.ctx.name) {
          API.getEntityType(_this.ctx.name, function(entityType) {
            return widget.template.render(entityType, function(err, html) {
              return callback(null, html);
            });
          });
        } else {
          i = 0;
          j = 0;
          result = "";
          while (true) {
            entityType = _this.ctx[i];
            if (entityType) {
              widget.template.render(entityType, function(err, html) {
                result += html;
                j++;
                if (i === j) {
                  return callback(null, result);
                }
              });
            } else {
              return;
            }
            i++;
          }
        }
      }
      if (widget.type === "Entity") {
        API.getEntities(_this.ctx.name, function(entities) {
          var len;
          result = "";
          i = 1;
          len = entities.length;
          return entities.forEach(function(entity) {
            entity.entityType = _this.ctx;
            return widget.template.render(entity, function(err, html) {
              result += html;
              if (i === len) {
                callback(null, result);
              }
              return i++;
            });
          });
        });
      }
      if (widget.type === "PropertyType") {
        entityType = _this.ctx;
        result = "";
        i = 1;
        len = entityType.propertyTypes.length;
        entityType.propertyTypes.forEach(function(propertyType) {
          return GUI.getWidget(port, entityType.name, propertyType, function(widget) {
            propertyType.entity = entityType;
            return widget.template.render(propertyType, function(err, html) {
              result += html;
              if (i === len) {
                callback(null, result);
              }
              return i++;
            });
          });
        });
      }
      if (widget.type === "Property") {
        entity = _this.ctx;
        entityType = _this.ctx.entityType ? _this.ctx.entityType : _this.ctx.type;
        result = "";
        i = 1;
        len = entityType.propertyTypes.length;
        return entityType.propertyTypes.forEach(function(propertyType) {
          return GUI.getWidget(port, entityType.name, propertyType, function(widget) {
            var property, propertyValue;
            propertyType.entity = entityType;
            propertyValue = entity[propertyType.name];
            property = {
              value: propertyValue,
              type: propertyType
            };
            return widget.template.render(property, function(err, html) {
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

  $(function() {
    env.addFilter('port', GUI.renderPortFilter, true);
    env.addFilter('newPage', GUI.renderNewPageFilter, false);
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
