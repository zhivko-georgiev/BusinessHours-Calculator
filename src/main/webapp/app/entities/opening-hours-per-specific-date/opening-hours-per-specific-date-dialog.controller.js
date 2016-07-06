(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('OpeningHoursPerSpecificDateDialogController', OpeningHoursPerSpecificDateDialogController);

    OpeningHoursPerSpecificDateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OpeningHoursPerSpecificDate', 'AlertService'];

    function OpeningHoursPerSpecificDateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OpeningHoursPerSpecificDate, AlertService) {
        var vm = this;

        vm.openingHoursPerSpecificDate = entity;
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
            if (vm.openingHoursPerSpecificDate.id !== null) {
                OpeningHoursPerSpecificDate.update(vm.openingHoursPerSpecificDate, onSaveSuccess, onSaveError);
            } else {
                OpeningHoursPerSpecificDate.save(vm.openingHoursPerSpecificDate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('businessHoursCalculatorApp:openingHoursPerSpecificDateUpdate', result);
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
