(function() {
  var EntityTitle,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityTitle = (function(_super) {

    __extends(EntityTitle, _super);

    function EntityTitle() {
      return EntityTitle.__super__.constructor.apply(this, arguments);
    }

    EntityTitle.prototype.template = function() {
      return "<h2 id='title_{{entityType.name}}'>\n  {{entityType.name}}\n</h2>";
    };

    return EntityTitle;

  })(EntitySetTemplate);

  return new EntityTitle;

}).call(this);
