(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('BusinessHoursCalculatorDialogController', BusinessHoursCalculatorDialogController);

    BusinessHoursCalculatorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BusinessHoursCalculator'];

    function BusinessHoursCalculatorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BusinessHoursCalculator) {
        var vm = this;

        vm.businessHoursCalculator = entity;
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
            if (vm.businessHoursCalculator.id !== null) {
                BusinessHoursCalculator.update(vm.businessHoursCalculator, onSaveSuccess, onSaveError);
            } else {
                BusinessHoursCalculator.save(vm.businessHoursCalculator, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('businessHoursCalculatorApp:businessHoursCalculatorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.expectedPickupTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
