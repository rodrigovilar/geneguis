(function() {
  var EntityTitle2,
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; },
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityTitle2 = (function(_super) {

    __extends(EntityTitle2, _super);

    function EntityTitle2() {
      this.renderList = __bind(this.renderList, this);

      this.renderTitle = __bind(this.renderTitle, this);
      return EntityTitle2.__super__.constructor.apply(this, arguments);
    }

    EntityTitle2.prototype.render = function(page, entityType) {
      this.renderTitle(page, entityType);
      return this.renderList(page, entityType);
    };

    EntityTitle2.prototype.renderTitle = function(page, entityType) {
      var title;
      title = View.createEl("<h2>", "title", entityType.name);
      title.append(entityType.name);
      return page.append(title);
    };

    EntityTitle2.prototype.renderList = function(page, entityType) {
      var listWidget;
      listWidget = RenderingEngine.getEntitySetWidget('list', entityType);
      return listWidget.render(page, entityType);
    };

    return EntityTitle2;

  })(EntitySetWidget);

  return new EntityTitle2;

}).call(this);
