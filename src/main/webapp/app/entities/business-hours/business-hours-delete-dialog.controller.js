(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('BusinessHoursDeleteController',BusinessHoursDeleteController);

    BusinessHoursDeleteController.$inject = ['$uibModalInstance', 'entity', 'BusinessHours'];

    function BusinessHoursDeleteController($uibModalInstance, entity, BusinessHours) {
        var vm = this;

        vm.businessHours = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BusinessHours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
