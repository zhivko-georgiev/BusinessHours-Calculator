(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('OpeningHoursPerSpecificDateDeleteController',OpeningHoursPerSpecificDateDeleteController);

    OpeningHoursPerSpecificDateDeleteController.$inject = ['$uibModalInstance', 'entity', 'OpeningHoursPerSpecificDate'];

    function OpeningHoursPerSpecificDateDeleteController($uibModalInstance, entity, OpeningHoursPerSpecificDate) {
        var vm = this;

        vm.openingHoursPerSpecificDate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OpeningHoursPerSpecificDate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
