(function() {
  var DateFormatterWidget,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  DateFormatterWidget = (function(_super) {

    __extends(DateFormatterWidget, _super);

    function DateFormatterWidget() {
      return DateFormatterWidget.__super__.constructor.apply(this, arguments);
    }

    DateFormatterWidget.prototype.render = function(view) {
      var format;
      format = "yy-mm-dd";
      if (this.configuration) {
        format = this.configuration.format;
      }
      return view.append($.datepicker.formatDate(format, new Date(this.property)));
    };

    return DateFormatterWidget;

  })(PropertyWidget);

  return new DateFormatterWidget;

}).call(this);
