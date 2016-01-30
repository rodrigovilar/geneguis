(function() {
  var EntityUnorderedList,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityUnorderedList = (function(_super) {

    __extends(EntityUnorderedList, _super);

    function EntityUnorderedList() {
      return EntityUnorderedList.__super__.constructor.apply(this, arguments);
    }

    EntityUnorderedList.prototype.render = function(page, entityType) {
      var list;
      list = View.createEl("<ul>", "list", entityType.name);
      page.append(list);
      return this.renderItems(list, entityType);
    };

    EntityUnorderedList.prototype.renderItems = function(list, entityType) {
      var itemWidget,
        _this = this;
      itemWidget = RenderingEngine.getEntityWidget('item', entityType);
      return DataManager.getEntities(entityType.name, function(entity) {
        return itemWidget.render(list, entityType, entity);
      });
    };

    return EntityUnorderedList;

  })(EntitySetWidget);

  return new EntityUnorderedList;

}).call(this);
