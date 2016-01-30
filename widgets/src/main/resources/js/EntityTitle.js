(function() {
  var EntityTitle,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityTitle = (function(_super) {

    __extends(EntityTitle, _super);

    function EntityTitle() {
      return EntityTitle.__super__.constructor.apply(this, arguments);
    }

    EntityTitle.prototype.render = function(page, entityType) {
      var title;
      title = View.createEl("<h2>", "title", entityType.name);
      title.append(entityType.name);
      return page.append(title);
    };

    return EntityTitle;

  })(EntitySetWidget);

  return new EntityTitle;

}).call(this);
