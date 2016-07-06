(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('StoreClosedPerSpecificDateDeleteController',StoreClosedPerSpecificDateDeleteController);

    StoreClosedPerSpecificDateDeleteController.$inject = ['$uibModalInstance', 'entity', 'StoreClosedPerSpecificDate'];

    function StoreClosedPerSpecificDateDeleteController($uibModalInstance, entity, StoreClosedPerSpecificDate) {
        var vm = this;

        vm.storeClosedPerSpecificDate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StoreClosedPerSpecificDate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
