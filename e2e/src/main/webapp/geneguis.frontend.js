(function() {
  var _this = this,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

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
    return widget;
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

  GUI.newPage = function(widget, entityTypeName) {
    var body, page;
    body = $("body");
    body.empty();
    page = $('<div>');
    page.attr("id", "page_view");
    body.append(page);
    page.rootWidget = widget;
    page.rootWidget.context = {
      name: entityTypeName
    };
    WidgetStack.push(page);
    return page;
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

  GUI.newPageForEntityTypeSet = function(port, entityTypeName, entityId) {
    console.log('newPageForEntityTypeSet: ' + port);
    return GUI.getWidget(port, entityTypeName, null, function(widget) {
      var view,
        _this = this;
      view = GUI.newPage(widget, entityTypeName);
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
    return GUI.newPageForEntityTypeSet('root');
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

  Filter.AbstractFilter = (function() {

    function AbstractFilter(name) {
      this.name = name;
    }

    AbstractFilter.prototype.renderNewPage = function(port, context) {
      var entityTypeName;
      entityTypeName = context.name;
      return "onClick=\"Filter.newPageFor" + this.name + "('" + port + "','" + entityTypeName + "')\"";
    };

    AbstractFilter.prototype.forEachPort = function(port, context, callback) {};

    AbstractFilter.prototype.forPort = function(port, context, callback) {
      var widget;
      console.log(this.name + '.forPort: ' + port);
      widget = this.getWidget(port, context);
      return this.renderFor(widget, context, callback);
    };

    AbstractFilter.prototype.getWidget = function(port, params) {
      var rule;
      rule = this.getRule(port, params);
      this.checkRule(rule);
      return GUI.getWidgetByRule(rule);
    };

    AbstractFilter.prototype.getRule = function(port, params) {};

    AbstractFilter.prototype.checkRule = function(rule) {
      if (rule) {
        if (rule.type === this.name) {

        } else {
          throw 'Problem on port #{port}: #{rule.name} is not #{@name}';
        }
      } else {
        throw 'No rules found for port: #{port}';
      }
    };

    AbstractFilter.prototype.renderFor = function(widget, context, callback) {};

    AbstractFilter.prototype.matchExpression = function(text, expression) {
      return new RegExp("^" + expression.split("*").join(".*") + "$").test(text);
    };

    return AbstractFilter;

  })();

  Filter.EntityTypeFilter = (function(_super) {

    __extends(EntityTypeFilter, _super);

    function EntityTypeFilter() {
      EntityTypeFilter.__super__.constructor.call(this, "EntityType");
    }

    EntityTypeFilter.prototype.newPage = function(port, entityType) {
      var view, widget;
      console.log(this.name + '.newPage: ' + port);
      widget = this.getWidget(port, {
        entityTypeName: entityType.name
      });
      view = GUI.newPage(widget, entityType.name);
      view.rootWidget.context = entityType;
      return view.rootWidget.template.render(entityType, function(err, html) {
        if (err) {
          throw err;
        }
        return view.html(html);
      });
    };

    EntityTypeFilter.prototype.getRule = function(port, params) {
      var defaultScope, matchName, rule, _i, _len;
      defaultScope = null;
      matchName = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if (rule.entityTypeLocator === "*") {
            defaultScope = rule;
          } else if (this.matchExpression(params.entityTypeName, rule.entityTypeLocator)) {
            matchName = rule;
          }
        }
      }
      if (matchName) {
        return matchName;
      }
      return defaultScope;
    };

    EntityTypeFilter.prototype.forEachPort = function(port, context, callback) {
      var current, item, result, size, widget, _results;
      console.log(this.name + '.forEachPort: ' + port);
      size = 0;
      result = "";
      current = 0;
      item = context[size];
      _results = [];
      while (item) {
        widget = this.getWidget(port, {
          entityTypeName: item.name
        });
        widget.template.render(item, function(err, html) {
          if (err) {
            throw err;
          }
          result += html;
          if (++current === size) {
            return callback(null, result);
          }
        });
        _results.push(item = context[++size]);
      }
      return _results;
    };

    EntityTypeFilter.prototype.renderFor = function(widget, context, callback) {
      var _this = this;
      return API.getEntityType(context.name, function(entityType) {
        return widget.template.render(entityType, function(err, html) {
          if (err) {
            throw err;
          }
          return callback(null, html);
        });
      });
    };

    return EntityTypeFilter;

  })(Filter.AbstractFilter);

  Filter.EntityFilter = (function(_super) {

    __extends(EntityFilter, _super);

    function EntityFilter() {
      EntityFilter.__super__.constructor.call(this, "Entity");
    }

    EntityFilter.prototype.renderNewPage = function(port, context) {
      var entityTypeName;
      entityTypeName = context.name;
      return "onClick=\"Filter.newPageFor" + this.name + "('" + port + "','" + entityTypeName + "'," + context.id + ")\"";
    };

    EntityFilter.prototype.newPage = function(port, entityType, entity) {
      var view, widget;
      console.log(this.name + '.newPage: ' + port);
      widget = this.getWidget(port, {
        entityTypeName: entityType.name
      });
      view = GUI.newPage(widget, entityTypeName);
      entity.entityType = entityType;
      view.rootWidget.context = entity;
      return view.rootWidget.template.render(entity, function(err, html) {
        if (err) {
          throw err;
        }
        return view.html(html);
      });
    };

    EntityFilter.prototype.getRule = function(port, params) {
      var defaultScope, matchName, rule, _i, _len;
      defaultScope = null;
      matchName = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if (rule.entityTypeLocator === "*") {
            defaultScope = rule;
          } else if (this.matchExpression(params.entityTypeName, rule.entityTypeLocator)) {
            matchName = rule;
          }
        }
      }
      if (matchName) {
        return matchName;
      }
      return defaultScope;
    };

    EntityFilter.prototype.forEachPort = function(port, context, callback) {
      var widget,
        _this = this;
      console.log(this.name + '.forEachPort: ' + port);
      widget = this.getWidget(port, {
        entityTypeName: context.name
      });
      return API.getEntities(context.name, function(entities) {
        var current, entity, result, size, _i, _len, _results;
        result = "";
        size = entities.length;
        current = 0;
        _results = [];
        for (_i = 0, _len = entities.length; _i < _len; _i++) {
          entity = entities[_i];
          entity.entityType = context;
          _results.push(widget.template.render(entity, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (++current === size) {
              return callback(null, result);
            }
          }));
        }
        return _results;
      });
    };

    EntityFilter.prototype.renderFor = function(widget, context, callback) {
      var _this = this;
      return API.getEntityType(context.name, function(entityType) {
        return widget.template.render(entityType, function(err, html) {
          if (err) {
            throw err;
          }
          return callback(null, html);
        });
      });
    };

    return EntityFilter;

  })(Filter.AbstractFilter);

  Filter.PropertyTypeFilter = (function(_super) {

    __extends(PropertyTypeFilter, _super);

    function PropertyTypeFilter() {
      PropertyTypeFilter.__super__.constructor.call(this, "PropertyType");
    }

    PropertyTypeFilter.prototype.getRule = function(port, params) {
      var defaultScope, fieldType, matchScope, matchType, rule, _i, _len;
      fieldType = params.fieldType;
      defaultScope = null;
      matchScope = null;
      matchType = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if ((rule.type === "PropertyType" || rule.type === "Property") && fieldType.kind === "Property") {
            if (rule.propertyTypeTypeLocator) {
              if (this.matchExpression(params.entityTypeName, rule.entityTypeLocator) && this.matchExpression(fieldType.name, rule.propertyTypeLocator) && rule.propertyTypeTypeLocator === fieldType.type) {
                matchType = rule;
              }
            } else {
              if (rule.entityTypeLocator === "*" & rule.propertyTypeLocator === "*") {
                defaultScope = rule;
              } else if (this.matchExpression(params.entityTypeName, rule.entityTypeLocator) && this.matchExpression(fieldType.name, rule.propertyTypeLocator)) {
                matchScope = rule;
              }
            }
          }
        }
      }
      if (matchType) {
        return matchType;
      }
      if (matchScope) {
        return matchScope;
      }
      return defaultScope;
    };

    PropertyTypeFilter.prototype.forEachPort = function(port, entityType, callback) {
      var current, fieldType, result, size, widget, _i, _len, _ref, _results;
      console.log(this.name + '.forEachPort: ' + port);
      result = "";
      current = 0;
      size = entityType.fieldTypes.length;
      _ref = entityType.fieldTypes;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        fieldType = _ref[_i];
        if (fieldType.kind === "Property") {
          widget = this.getWidget(port, {
            entityTypeName: entityType.name,
            fieldType: fieldType
          });
          fieldType.entity = entityType;
          _results.push(widget.template.render(fieldType, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (++current === size) {
              return callback(null, result);
            }
          }));
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };

    return PropertyTypeFilter;

  })(Filter.AbstractFilter);

  Filter.RelationshipTypeFilter = (function(_super) {

    __extends(RelationshipTypeFilter, _super);

    function RelationshipTypeFilter() {
      RelationshipTypeFilter.__super__.constructor.call(this, "RelationshipType");
    }

    RelationshipTypeFilter.prototype.getRule = function(port, params) {
      var fieldType, found, rule, _i, _len;
      fieldType = params.fieldType;
      found = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if ((rule.type === "RelationshipType" || rule.type === "Relationship" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Relationship") {
            found = rule;
          }
        }
      }
      return found;
    };

    RelationshipTypeFilter.prototype.forEachPort = function(port, entityType, callback) {
      var current, fieldType, result, size, widget, _i, _len, _ref, _results;
      console.log(this.name + '.forEachPort: ' + port);
      result = "";
      current = 0;
      size = entityType.fieldTypes.length;
      _ref = entityType.fieldTypes;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        fieldType = _ref[_i];
        if (fieldType.kind === "Relationship") {
          widget = this.getWidget(port, {
            entityTypeName: entityType.name,
            fieldType: fieldType
          });
          fieldType.entity = entityType;
          _results.push(widget.template.render(fieldType, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (++current === size) {
              return callback(null, result);
            }
          }));
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };

    return RelationshipTypeFilter;

  })(Filter.AbstractFilter);

  Filter.FieldTypeFilter = (function(_super) {

    __extends(FieldTypeFilter, _super);

    function FieldTypeFilter() {
      FieldTypeFilter.__super__.constructor.call(this, "FieldType");
    }

    FieldTypeFilter.prototype.getRule = function(port, params) {
      var fieldType, found, rule, _i, _len;
      fieldType = params.fieldType;
      found = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if ((rule.type === "PropertyType" || rule.type === "Property" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Property") {
            found = rule;
          }
          if ((rule.type === "RelationshipType" || rule.type === "Relationship" || rule.type === "FieldType" || rule.type === "Field") && fieldType.kind === "Relationship") {
            found = rule;
          }
        }
      }
      return found;
    };

    FieldTypeFilter.prototype.forEachPort = function(port, entityType, callback) {
      var current, fieldType, result, size, widget, _i, _len, _ref, _results;
      console.log(this.name + '.forEachPort: ' + port);
      result = "";
      current = 0;
      size = entityType.fieldTypes.length;
      _ref = entityType.fieldTypes;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        fieldType = _ref[_i];
        if (fieldType.kind === "Property") {
          widget = this.getWidget(port, {
            entityTypeName: entityType.name,
            fieldType: fieldType
          });
          fieldType.entity = entityType;
          _results.push(widget.template.render(fieldType, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (++current === size) {
              return callback(null, result);
            }
          }));
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };

    return FieldTypeFilter;

  })(Filter.AbstractFilter);

  Filter.PropertyFilter = (function(_super) {

    __extends(PropertyFilter, _super);

    function PropertyFilter() {
      PropertyFilter.__super__.constructor.call(this, "Property");
    }

    PropertyFilter.prototype.getRule = function(port, params) {
      var defaultScope, fieldType, matchScope, matchType, rule, _i, _len;
      fieldType = params.fieldType;
      defaultScope = null;
      matchScope = null;
      matchType = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if ((rule.type === "PropertyType" || rule.type === "Property") && fieldType.kind === "Property") {
            if (rule.propertyTypeTypeLocator) {
              if (this.matchExpression(params.entityTypeName, rule.entityTypeLocator) && this.matchExpression(fieldType.name, rule.propertyTypeLocator) && rule.propertyTypeTypeLocator === fieldType.type) {
                matchType = rule;
              }
            } else {
              if (rule.entityTypeLocator === "*" & rule.propertyTypeLocator === "*") {
                defaultScope = rule;
              } else if (this.matchExpression(params.entityTypeName, rule.entityTypeLocator) && this.matchExpression(fieldType.name, rule.propertyTypeLocator)) {
                matchScope = rule;
              }
            }
          }
        }
      }
      if (matchType) {
        return matchType;
      }
      if (matchScope) {
        return matchScope;
      }
      return defaultScope;
    };

    PropertyFilter.prototype.forEachPort = function(port, entity, callback) {
      var current, fieldType, property, propertyValue, result, size, widget, _i, _len, _ref, _results;
      console.log(this.name + '.forEachPort: ' + port);
      result = "";
      current = 0;
      size = entity.entityType.fieldTypes.length;
      _ref = entity.entityType.fieldTypes;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        fieldType = _ref[_i];
        if (fieldType.kind === "Property") {
          widget = this.getWidget(port, {
            entityTypeName: entity.entityType.name,
            fieldType: fieldType
          });
          fieldType.entity = entity.entityType;
          propertyValue = entity[fieldType.name];
          property = {
            value: propertyValue,
            type: fieldType
          };
          _results.push(widget.template.render(property, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (++current === size) {
              return callback(null, result);
            }
          }));
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };

    return PropertyFilter;

  })(Filter.AbstractFilter);

  Filter.RelationshipFilter = (function(_super) {

    __extends(RelationshipFilter, _super);

    function RelationshipFilter() {
      RelationshipFilter.__super__.constructor.call(this, "Relationship");
    }

    RelationshipFilter.prototype.getRule = function(port, params) {
      var fieldType, found, rule, _i, _len;
      fieldType = params.fieldType;
      found = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if ((rule.type === "RelationshipType" || rule.type === "Relationship") && fieldType.kind === "Relationship") {
            found = rule;
          }
        }
      }
      return found;
    };

    RelationshipFilter.prototype.forEachPort = function(port, entity, callback) {
      var current, fieldType, relationshipId, result, size, widget, _i, _len, _ref, _results,
        _this = this;
      console.log(this.name + '.forEachPort: ' + port);
      result = "";
      current = 0;
      size = entity.entityType.fieldTypes.length;
      _ref = entity.entityType.fieldTypes;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        fieldType = _ref[_i];
        if (fieldType.kind === "Relationship") {
          widget = this.getWidget(port, {
            entityTypeName: entity.entityType.name,
            fieldType: fieldType
          });
          fieldType.entity = entity.entityType;
          relationshipId = entity[fieldType.name];
          _results.push(API.getEntity(fieldType.targetType, relationshipId, function(entity) {
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
              if (++current === size) {
                return callback(null, result);
              }
            });
          }));
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };

    return RelationshipFilter;

  })(Filter.AbstractFilter);

  Filter.FieldFilter = (function(_super) {

    __extends(FieldFilter, _super);

    function FieldFilter() {
      FieldFilter.__super__.constructor.call(this, "Field");
    }

    FieldFilter.prototype.getRule = function(port, params) {
      var fieldType, found, rule, _i, _len;
      fieldType = params.fieldType;
      found = null;
      for (_i = 0, _len = RulesCache.length; _i < _len; _i++) {
        rule = RulesCache[_i];
        if (rule.portName === port) {
          if ((rule.type === "PropertyType" || rule.type === "Property") && fieldType.kind === "Property") {
            found = rule;
          }
          if ((rule.type === "RelationshipType" || rule.type === "Relationship") && fieldType.kind === "Relationship") {
            found = rule;
          }
        }
      }
      return found;
    };

    FieldFilter.prototype.forEachPort = function(port, entity, callback) {
      var current, fieldType, property, propertyValue, relationshipId, result, size, widget, _i, _len, _ref, _results,
        _this = this;
      console.log(this.name + '.forEachPort: ' + port);
      result = "";
      current = 0;
      size = entity.entityType.fieldTypes.length;
      _ref = entity.entityType.fieldTypes;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        fieldType = _ref[_i];
        widget = this.getWidget(port, {
          entityTypeName: entity.entityType.name,
          fieldType: fieldType
        });
        fieldType.entity = entity.entityType;
        if (fieldType.kind === "Property") {
          propertyValue = entity[fieldType.name];
          property = {
            value: propertyValue,
            type: fieldType
          };
          _results.push(widget.template.render(property, function(err, html) {
            if (err) {
              throw err;
            }
            result += html;
            if (++current === size) {
              return callback(null, result);
            }
          }));
        } else {
          relationshipId = entity[fieldType.name];
          _results.push(API.getEntity(fieldType.targetType, relationshipId, function(entity) {
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
              if (++current === size) {
                return callback(null, result);
              }
            });
          }));
        }
      }
      return _results;
    };

    return FieldFilter;

  })(Filter.AbstractFilter);

  Filter.renderNewPageForEntityType = function(port) {
    var context, filter;
    filter = new Filter.EntityTypeFilter;
    context = this.ctx;
    return filter.renderNewPage(port, context);
  };

  Filter.newPageForEntityType = function(port, entityTypeName) {
    var filter,
      _this = this;
    filter = new Filter.EntityTypeFilter;
    return API.getEntityType(entityTypeName, function(entityType) {
      return filter.newPage(port, entityType);
    });
  };

  Filter.forEachEntityType = function(port, callback) {
    var context, filter;
    filter = new Filter.EntityTypeFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.forEntityType = function(port, callback) {
    var context, filter;
    filter = new Filter.EntityTypeFilter;
    context = this.ctx;
    return filter.forPort(port, context, callback);
  };

  Filter.renderNewPageForEntity = function(port) {
    var context, filter;
    filter = new Filter.EntityFilter;
    context = this.ctx;
    return filter.renderNewPage(port, context);
  };

  Filter.forEachEntity = function(port, callback) {
    var context, filter;
    filter = new Filter.EntityFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.newPageForEntity = function(port, entityTypeName, entityId) {
    var filter,
      _this = this;
    filter = new Filter.EntityFilter;
    return API.getEntityType(entityTypeName, function(entityType) {
      return API.getEntity(entityTypeName, entityId, function(entity) {
        return filter.newPage(port, entityType, entity);
      });
    });
  };

  Filter.forEachPropertyType = function(port, callback) {
    var context, filter;
    filter = new Filter.PropertyTypeFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.forEachRelationshipType = function(port, callback) {
    var context, filter;
    filter = new Filter.RelationshipTypeFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.forEachFieldType = function(port, callback) {
    var context, filter;
    filter = new Filter.FieldTypeFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.forEachProperty = function(port, callback) {
    var context, filter;
    filter = new Filter.PropertyFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.forEachRelationship = function(port, callback) {
    var context, filter;
    filter = new Filter.RelationshipFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
  };

  Filter.forEachField = function(port, callback) {
    var context, filter;
    filter = new Filter.FieldFilter;
    context = this.ctx;
    return filter.forEachPort(port, context, callback);
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
