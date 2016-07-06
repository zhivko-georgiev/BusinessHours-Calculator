(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('StoreClosedPerDaysOfWeekDeleteController',StoreClosedPerDaysOfWeekDeleteController);

    StoreClosedPerDaysOfWeekDeleteController.$inject = ['$uibModalInstance', 'entity', 'StoreClosedPerDaysOfWeek'];

    function StoreClosedPerDaysOfWeekDeleteController($uibModalInstance, entity, StoreClosedPerDaysOfWeek) {
        var vm = this;

        vm.storeClosedPerDaysOfWeek = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StoreClosedPerDaysOfWeek.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
