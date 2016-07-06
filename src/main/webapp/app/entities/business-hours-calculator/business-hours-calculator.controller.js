(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('BusinessHoursCalculatorController', BusinessHoursCalculatorController);

    BusinessHoursCalculatorController.$inject = ['$scope', '$state', 'BusinessHoursCalculator'];

    function BusinessHoursCalculatorController ($scope, $state, BusinessHoursCalculator) {
        var vm = this;
        
        vm.businessHoursCalculators = [];

        loadAll();

        function loadAll() {
            BusinessHoursCalculator.query(function(result) {
                vm.businessHoursCalculators = result;
            });
        }
    }
})();
