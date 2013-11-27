/* global _:false, $j:false */

window.SS = typeof window.SS === 'object' ? window.SS : {};

(function() {

  var DetailsMetricFilterView = window.SS.DetailsFilterView.extend({
    template: '#metricFilterTemplate',


    events: {
      'change :input': 'inputChanged'
    },


    inputChanged: function() {
      var value = {
        metric: this.$('[name=metric]').val(),
        metricText: this.$('[name=metric] option:selected').text(),
        period: this.$('[name=period]').val(),
        periodText: this.$('[name=period] option:selected').text(),
        op: this.$('[name=op]').val(),
        opText: this.$('[name=op] option:selected').text(),
        val: this.$('[name=val]').val()
      };
      this.model.set('value', value);
    },


    onRender: function() {
      var value = this.model.get('value') || {};
      this.$('[name=metric]').val(value.metric);
      this.$('[name=period]').val(value.period);
      this.$('[name=op]').val(value.op);
      this.$('[name=val]').val(value.val);
      this.inputChanged();
    }

  });



  var MetricFilterView = window.SS.BaseFilterView.extend({

    initialize: function() {
      window.SS.BaseFilterView.prototype.initialize.call(this, {
        detailsView: DetailsMetricFilterView
      });

      this.groupMetrics();
    },


    groupMetrics: function() {
      var metrics = _.map(this.model.get('metrics'), function (metric) {
            return metric.metric;
          }),
          groupedMetrics =
              _.sortBy(
                  _.map(
                      _.groupBy(metrics, 'domain'),
                      function (metrics, domain) {
                        return {
                          domain: domain,
                          metrics: _.sortBy(metrics, 'short_name')
                        };
                      }),
                  'domain'
              );
      this.model.set('groupedMetrics', groupedMetrics);
    },


    renderValue: function() {
      return this.isDefaultValue() ?
          'Not set' :
          this.model.get('value').metricText + ' ' + this.model.get('value').opText + ' ' + this.model.get('value').val;
    },


    renderInput: function() {
      var that = this,
          value = this.model.get('value') || {};
      _.each(value, function(v, k) {

        $j('<input>')
            .prop('name', that.model.get('property') + '_' + k)
            .prop('type', 'hidden')
            .css('display', 'none')
            .val(v)
            .appendTo(that.$el);
      });
    },


    isDefaultValue: function() {
      var value = this.model.get('value');
      if (!_.isObject(value)) {
        return true;
      }
      return !(value.metric && value.period && value.op && value.val);
    },


    restoreFromQuery: function(q) {
      var that = this,
          value = {};
      _.each(['metric', 'period', 'op', 'val'], function(p) {
        var property = that.model.get('property') + '_' + p,
            pValue = _.findWhere(q, { key: property });

        if (pValue.value) {
          value[p] = pValue.value;
        }
      });

      if (value && value.metric && value.period && value.op && value.val) {
        this.model.set({
          value: value,
          enabled: true
        });
      }
    }

  });



  /*
   * Export public classes
   */

  _.extend(window.SS, {
    MetricFilterView: MetricFilterView
  });

})();