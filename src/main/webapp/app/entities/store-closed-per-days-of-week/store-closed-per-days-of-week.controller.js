(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('StoreClosedPerDaysOfWeekController', StoreClosedPerDaysOfWeekController);

    StoreClosedPerDaysOfWeekController.$inject = ['$scope', '$state', 'StoreClosedPerDaysOfWeek'];

    function StoreClosedPerDaysOfWeekController ($scope, $state, StoreClosedPerDaysOfWeek) {
        var vm = this;
        
        vm.storeClosedPerDaysOfWeeks = [];

        loadAll();

        function loadAll() {
            StoreClosedPerDaysOfWeek.query(function(result) {
                vm.storeClosedPerDaysOfWeeks = result;
            });
        }
    }
})();
