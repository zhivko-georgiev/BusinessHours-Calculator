(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('StoreClosedPerDaysOfWeekDialogController', StoreClosedPerDaysOfWeekDialogController);

    StoreClosedPerDaysOfWeekDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StoreClosedPerDaysOfWeek', 'AlertService'];

    function StoreClosedPerDaysOfWeekDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StoreClosedPerDaysOfWeek, AlertService) {
        var vm = this;

        vm.storeClosedPerDaysOfWeek = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.storeClosedPerDaysOfWeek.id !== null) {
                StoreClosedPerDaysOfWeek.update(vm.storeClosedPerDaysOfWeek, onSaveSuccess, onSaveError);
            } else {
                StoreClosedPerDaysOfWeek.save(vm.storeClosedPerDaysOfWeek, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('businessHoursCalculatorApp:storeClosedPerDaysOfWeekUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (error) {
            AlertService.error(error.data.message);
            vm.isSaving = false;
        }


    }
})();
