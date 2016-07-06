(function() {
    'use strict';
    angular
        .module('businessHoursCalculatorApp')
        .factory('StoreClosedPerDaysOfWeek', StoreClosedPerDaysOfWeek);

    StoreClosedPerDaysOfWeek.$inject = ['$resource'];

    function StoreClosedPerDaysOfWeek ($resource) {
        var resourceUrl =  'api/store-closed-per-day-of-weeks/:id';

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
