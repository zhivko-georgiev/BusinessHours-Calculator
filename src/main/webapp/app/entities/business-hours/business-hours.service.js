(function() {
    'use strict';
    angular
        .module('businessHoursCalculatorApp')
        .factory('BusinessHours', BusinessHours);

    BusinessHours.$inject = ['$resource'];

    function BusinessHours ($resource) {
        var resourceUrl =  'api/business-hours/:id';

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
