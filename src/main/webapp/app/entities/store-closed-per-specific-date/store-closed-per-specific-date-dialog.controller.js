(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('StoreClosedPerSpecificDateDialogController', StoreClosedPerSpecificDateDialogController);

    StoreClosedPerSpecificDateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StoreClosedPerSpecificDate', 'AlertService'];

    function StoreClosedPerSpecificDateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StoreClosedPerSpecificDate, AlertService) {
        var vm = this;

        vm.storeClosedPerSpecificDate = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.storeClosedPerSpecificDate.id !== null) {
                StoreClosedPerSpecificDate.update(vm.storeClosedPerSpecificDate, onSaveSuccess, onSaveError);
            } else {
                StoreClosedPerSpecificDate.save(vm.storeClosedPerSpecificDate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('businessHoursCalculatorApp:storeClosedPerSpecificDateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (error) {
            AlertService.error(error.data.message);
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
