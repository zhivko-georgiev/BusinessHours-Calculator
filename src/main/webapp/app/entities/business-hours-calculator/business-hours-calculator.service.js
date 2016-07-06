(function() {
    'use strict';
    angular
        .module('businessHoursCalculatorApp')
        .factory('BusinessHoursCalculator', BusinessHoursCalculator);

    BusinessHoursCalculator.$inject = ['$resource', 'DateUtils'];

    function BusinessHoursCalculator ($resource, DateUtils) {
        var resourceUrl =  'api/business-hours-calculators/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.expectedPickupTime = DateUtils.convertDateTimeFromServer(data.expectedPickupTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
