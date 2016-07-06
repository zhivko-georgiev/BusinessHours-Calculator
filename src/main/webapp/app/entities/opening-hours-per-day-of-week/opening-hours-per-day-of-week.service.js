(function() {
    'use strict';
    angular
        .module('businessHoursCalculatorApp')
        .factory('OpeningHoursPerDayOfWeek', OpeningHoursPerDayOfWeek);

    OpeningHoursPerDayOfWeek.$inject = ['$resource'];

    function OpeningHoursPerDayOfWeek ($resource) {
        var resourceUrl =  'api/opening-hours-per-day-of-weeks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
