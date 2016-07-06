(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('BusinessHoursController', BusinessHoursController);

    BusinessHoursController.$inject = ['$scope', '$state', 'BusinessHours'];

    function BusinessHoursController ($scope, $state, BusinessHours) {
        var vm = this;
        
        vm.businessHours = [];

        loadAll();

        function loadAll() {
            BusinessHours.query(function(result) {
                vm.businessHours = result;
            });
        }
    }
})();
