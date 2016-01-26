(function() {
  var _this = this,
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

  window.RootRenderer = (function() {

    function RootRenderer() {}

    RootRenderer.prototype.render = function(view, entitesType) {
      var table, th,
        _this = this;
      table = $("<table>");
      view.append(table);
      th = $("<tr><th>Entities</th></tr>");
      th.attr("id", "entities");
      table.append(th);
      return entitesType.forEach(function(entityType) {
        return _this.drawLine(table, entityType);
      });
    };

    RootRenderer.prototype.drawLine = function(table, entityType) {
      var tr,
        _this = this;
      tr = $("<tr><td>" + entityType.name + "</td></tr>");
      tr.attr("id", "entityType_" + entityType.name);
      table.append(tr);
      return tr.click(function() {
        var widget;
        widget = RenderingEngine.getEntitySetWidget('root', entityType);
        return DataManager.getEntityType(entityType.id, function(entityTypeFull) {
          widget.entityType = entityTypeFull;
          return widget.render(View.emptyPage());
        });
      });
    };

    return RootRenderer;

  })();

  window.View = {};

  View.emptyPage = function() {
    var body, page;
    body = $("body");
    body.empty();
    page = $('<div>');
    body.append(page);
    return page;
  };

  window.WidgetManager = {};

  WidgetManager.STORAGE_TAG = "WIDGETS";

  WidgetManager.downloadAllWidgets = function() {
    var _this = this;
    return $.getJSON(HOST + 'widgets', function(widgetsSpec) {
      return widgetsSpec.forEach(function(widgetSpec) {
        var widget;
        simpleStorage.set(WidgetManager.STORAGE_TAG + widgetSpec.id + widgetSpec.version, widgetSpec);
        widget = eval(widgetSpec.code);
        if (widget.require && !window[widget.require.name]) {
          return $.getScript(widget.require.url);
        }
      });
    });
  };

  WidgetManager.getWidget = function(id, version) {
    var widget, widgetSpec;
    widgetSpec = simpleStorage.get(WidgetManager.STORAGE_TAG + id + version);
    widget = eval(widgetSpec.code);
    widget.id = widgetSpec.id;
    widget.version = widgetSpec.version;
    widget.name = widgetSpec.name;
    widget.type = widgetSpec.type;
    return widget;
  };

  WidgetManager.getRootRenderer = function(callback) {
    return callback(new RootRenderer);
  };

  window.DataManager = {};

  DataManager.loadData = function(url, callback) {
    var _this = this;
    return $.getJSON(HOST + url, function(json) {
      return callback(json);
    });
  };

  DataManager.getAllEntitiesTypes = function(callback) {
    return DataManager.loadData('entities', callback);
  };

  DataManager.getEntityType = function(entityId, callback) {
    return DataManager.loadData('entities/' + entityId, callback);
  };

  DataManager.getEntities = function(entityTypeResource, callback) {
    return DataManager.loadData('api/' + entityTypeResource, callback);
  };

  DataManager.getEntity = function(entityTypeResource, entityID, callback) {
    return DataManager.loadData('api/' + entityTypeResource + '/' + entityID, callback);
  };

  DataManager.createEntity = function(entityTypeResource, entity) {
    return $.ajax({
      url: HOST + "api/" + entityTypeResource,
      type: "POST",
      data: JSON.stringify(entity),
      contentType: "application/json; charset=utf-8"
    });
  };

  DataManager.updateEntity = function(entityTypeResource, entity) {
    return $.ajax({
      url: HOST + "api/" + entityTypeResource + "/" + entity.id,
      type: "PUT",
      data: JSON.stringify(entity)
    });
  };

  DataManager.deleteEntity = function(entityTypeResource, entityID, success, error) {
    return $.ajax({
      url: HOST + "api/" + entityTypeResource + "/" + entityID,
      type: "DELETE"
    });
  };

  window.RulesManager = {};

  RulesManager.STORAGE_TAG = "RULES";

  RulesManager.SEPARATOR_CHAR = '.';

  RulesManager.stringfyRule = function(rule) {
    var stringRule;
    stringRule = RulesManager.STORAGE_TAG + RulesManager.SEPARATOR_CHAR + rule.providedContext.name;
    if (rule.entityTypeLocator !== null) {
      stringRule += RulesManager.SEPARATOR_CHAR + rule.entityTypeLocator;
    } else {
      stringRule += RulesManager.SEPARATOR_CHAR + '*';
    }
    if (rule.providedContext.type === 'Property' || rule.providedContext.type === 'Relationship') {
      if (rule.propertyTypeLocator !== null) {
        stringRule += RulesManager.SEPARATOR_CHAR + rule.propertyTypeLocator;
      } else {
        stringRule += RulesManager.SEPARATOR_CHAR + '*';
      }
      if (rule.propertyTypeTypeLocator !== null) {
        stringRule += RulesManager.SEPARATOR_CHAR + rule.propertyTypeTypeLocator;
      } else {
        stringRule += RulesManager.SEPARATOR_CHAR + '*';
      }
      if (rule.providedContext.type === 'Relationship') {
        if (rule.relationshipTargetCardinality !== null) {
          stringRule += RulesManager.SEPARATOR_CHAR + rule.relationshipTargetCardinality;
        } else {
          stringRule += RulesManager.SEPARATOR_CHAR + '*';
        }
      }
    }
    return stringRule;
  };

  RulesManager.createRule = function(contextName, contextType, entityTypeLocator, propertyTypeLocator, propertyTypeTypeLocator, relationshipTargetCardinality) {
    var rule;
    rule = {};
    rule.providedContext = {};
    rule.providedContext.name = contextName;
    rule.providedContext.type = contextType;
    rule.entityTypeLocator = entityTypeLocator;
    rule.propertyTypeLocator = propertyTypeLocator;
    if (entityTypeLocator === null || propertyTypeLocator === null) {
      rule.propertyTypeTypeLocator = propertyTypeTypeLocator;
      rule.relationshipTargetCardinality = relationshipTargetCardinality;
    }
    return rule;
  };

  RulesManager.downloadAllRules = function() {
    var _this = this;
    return $.getJSON(HOST + 'rules', function(rules) {
      return rules.forEach(function(rule) {
        var key;
        if (rule.providedContext.type === 'Property' || rule.providedContext.type === 'Relationship') {
          if (rule.entityTypeLocator !== null && rule.propertyTypeLocator !== null) {
            rule.propertyTypeTypeLocator = null;
            rule.relationshipTargetCardinality = null;
          }
        }
        key = RulesManager.stringfyRule(rule);
        return simpleStorage.set(key, rule);
      });
    });
  };

  RulesManager.getRule = function(contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality) {
    var rule, ruleQuery;
    ruleQuery = RulesManager.createRule(contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality);
    if (ruleQuery.providedContext.type === 'Property') {
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, propertyTypeType, null);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, null, null);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, propertyTypeType, null);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, null, null);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, null, propertyTypeType, null);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, null, null, null);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
    } else if (ruleQuery.providedContext.type === 'Relationship') {
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, propertyTypeType, relationshipTargetCardinality);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, entityType, null, null, relationshipTargetCardinality);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, propertyTypeType, relationshipTargetCardinality);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, propertyType, null, relationshipTargetCardinality);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, null, propertyTypeType, relationshipTargetCardinality);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery = RulesManager.createRule(contextName, contextType, null, null, null, relationshipTargetCardinality);
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
    } else {
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
      ruleQuery.entityTypeLocator = '*';
      rule = simpleStorage.get(RulesManager.stringfyRule(ruleQuery));
      if (typeof rule !== "undefined") {
        return rule;
      }
    }
  };

  window.HOST = 'http://localhost:8081/';

  window.RenderingEngine = {};

  window.WidgetStack = [];

  Date.prototype.toISO8601 = function() {
    self = this;
    if (!isNaN(self)) {
      return self.toISOString().slice(0, 19) + 'Z';
    }
  };

  Date.prototype.toJSON = function() {
    return this.toISO8601();
  };

  RenderingEngine.pushWidget = function(widget) {
    return WidgetStack.push(widget);
  };

  RenderingEngine.popAndRender = function(view) {
    return WidgetStack.pop().render(view);
  };

  RenderingEngine.openApp = function(view) {
    var _this = this;
    return WidgetManager.getRootRenderer(function(rootRenderer) {
      return DataManager.getAllEntitiesTypes(function(allEntitiesTypes) {
        return rootRenderer.render(view, allEntitiesTypes);
      });
    });
  };

  RenderingEngine.getWidget = function(contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality) {
    var rule, widget;
    rule = RulesManager.getRule(contextName, contextType, entityType, propertyType, propertyTypeType, relationshipTargetCardinality);
    widget = WidgetManager.getWidget(rule.widget.id, rule.widget.version);
    widget.configuration = $.parseJSON(rule.configuration);
    return widget;
  };

  RenderingEngine.getRelationshipWidget = function(context, entityType, relationshipType) {
    return RenderingEngine.getWidget(context, 'Relationship', entityType.name, relationshipType.name, relationshipType.targetType.name, relationshipType.targetCardinality);
  };

  RenderingEngine.getPropertyWidget = function(context, entityType, propertyType) {
    return RenderingEngine.getWidget(context, 'Property', entityType.name, propertyType.name, propertyType.type, null);
  };

  RenderingEngine.getEntityWidget = function(context, entityType) {
    return RenderingEngine.getWidget(context, 'Entity', entityType.name, null, null, null);
  };

  RenderingEngine.getEntitySetWidget = function(context, entityType) {
    return RenderingEngine.getWidget(context, 'Entity', entityType.name, null, null, null);
  };

  $(function() {
    var _this = this;
    return $.getScript("https://dl.dropboxusercontent.com/u/14874989/Mestrado/metaguiweb/js/simpleStorage.js", function() {
      return $.getScript("https://dl.dropboxusercontent.com/u/14874989/mestrado/metaguiweb/js/jquery.mask.min.js", function() {
        RulesManager.downloadAllRules();
        WidgetManager.downloadAllWidgets();
        return RenderingEngine.openApp(View.emptyPage());
      });
    });
  });

  window.EntitySetWidget = (function() {

    function EntitySetWidget() {}

    EntitySetWidget.prototype.render = function(view) {};

    return EntitySetWidget;

  })();

  window.EntityWidget = (function() {

    function EntityWidget() {}

    EntityWidget.prototype.render = function(view) {};

    return EntityWidget;

  })();

  window.PropertyWidget = (function() {

    function PropertyWidget() {
      this.createInputElement = __bind(this.createInputElement, this);

    }

    PropertyWidget.prototype.render = function(view) {};

    PropertyWidget.prototype.injectValue = function(entity) {};

    PropertyWidget.prototype.createInputElement = function() {
      var input;
      input = $("<input>");
      input.attr("id", this.propertyType.name);
      return input;
    };

    return PropertyWidget;

  })();

  window.RelationshipWidget = (function() {

    function RelationshipWidget() {}

    RelationshipWidget.prototype.render = function(view, relationType, relation) {};

    RelationshipWidget.prototype.populateSelectField = function(selectField, resource, propertyKey, relationshipIds) {
      var _this = this;
      return DataManager.getEntities(resource, function(entities) {
        return entities.forEach(function(entity) {
          var option;
          option = new Option(entity.id);
          if (propertyKey) {
            option = new Option(entity[propertyKey], entity.id);
          }
          selectField.append(option);
          if (relationshipIds && relationshipIds.indexOf(entity.id) !== -1) {
            return option.selected = true;
          }
        });
      });
    };

    return RelationshipWidget;

  })();

}).call(this);
