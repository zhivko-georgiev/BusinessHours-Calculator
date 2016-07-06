(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('StoreClosedPerSpecificDateController', StoreClosedPerSpecificDateController);

    StoreClosedPerSpecificDateController.$inject = ['$scope', '$state', 'StoreClosedPerSpecificDate'];

    function StoreClosedPerSpecificDateController ($scope, $state, StoreClosedPerSpecificDate) {
        var vm = this;
        
        vm.storeClosedPerSpecificDates = [];

        loadAll();

        function loadAll() {
            StoreClosedPerSpecificDate.query(function(result) {
                vm.storeClosedPerSpecificDates = result;
            });
        }
    }
})();
