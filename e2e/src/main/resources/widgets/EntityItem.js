(function() {
  var EntityItem,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityItem = (function(_super) {

    __extends(EntityItem, _super);

    function EntityItem() {
      return EntityItem.__super__.constructor.apply(this, arguments);
    }

    EntityItem.prototype.render = function(list, entityType, entity) {
      var item;
      item = View.createEl("<li>" + entity + "</li>", "li", entity.id);
      return list.append(item);
    };

    return EntityItem;

  })(EntityWidget);

  return new EntityItem;

}).call(this);
