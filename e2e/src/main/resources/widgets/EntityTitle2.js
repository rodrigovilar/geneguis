(function() {
  var EntityTitle2,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityTitle2 = (function(_super) {

    __extends(EntityTitle2, _super);

    function EntityTitle2() {
      return EntityTitle2.__super__.constructor.apply(this, arguments);
    }

    EntityTitle2.prototype.template = function() {
      return "<h2 id='title_{{name}}'>\n  {{name}}\n</h2>\n{{renderEntitySet 'list' name}}";
    };

    return EntityTitle2;

  })(EntitySetTemplate);

  return new EntityTitle2;

}).call(this);
