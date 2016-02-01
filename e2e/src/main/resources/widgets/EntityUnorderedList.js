(function() {
  var EntityUnorderedList,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  EntityUnorderedList = (function(_super) {

    __extends(EntityUnorderedList, _super);

    function EntityUnorderedList() {
      return EntityUnorderedList.__super__.constructor.apply(this, arguments);
    }

    EntityUnorderedList.prototype.template = function() {
      return "<ul id='list_{{name}}'> \n  {{renderEntities 'item' name}}\n</ul>";
    };

    return EntityUnorderedList;

  })(EntitySetTemplate);

  return new EntityUnorderedList;

}).call(this);
