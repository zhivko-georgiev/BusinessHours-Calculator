(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('OpeningHoursPerDayOfWeekDeleteController',OpeningHoursPerDayOfWeekDeleteController);

    OpeningHoursPerDayOfWeekDeleteController.$inject = ['$uibModalInstance', 'entity', 'OpeningHoursPerDayOfWeek'];

    function OpeningHoursPerDayOfWeekDeleteController($uibModalInstance, entity, OpeningHoursPerDayOfWeek) {
        var vm = this;

        vm.openingHoursPerDayOfWeek = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OpeningHoursPerDayOfWeek.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
