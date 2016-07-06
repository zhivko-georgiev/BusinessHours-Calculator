(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('BusinessHoursCalculatorDeleteController',BusinessHoursCalculatorDeleteController);

    BusinessHoursCalculatorDeleteController.$inject = ['$uibModalInstance', 'entity', 'BusinessHoursCalculator'];

    function BusinessHoursCalculatorDeleteController($uibModalInstance, entity, BusinessHoursCalculator) {
        var vm = this;

        vm.businessHoursCalculator = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BusinessHoursCalculator.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
