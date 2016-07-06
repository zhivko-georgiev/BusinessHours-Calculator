(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('OpeningHoursPerDayOfWeekDialogController', OpeningHoursPerDayOfWeekDialogController);

    OpeningHoursPerDayOfWeekDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OpeningHoursPerDayOfWeek', 'AlertService'];

    function OpeningHoursPerDayOfWeekDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OpeningHoursPerDayOfWeek, AlertService) {
        var vm = this;

        vm.openingHoursPerDayOfWeek = entity;
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
            if (vm.openingHoursPerDayOfWeek.id !== null) {
                OpeningHoursPerDayOfWeek.update(vm.openingHoursPerDayOfWeek, onSaveSuccess, onSaveError);
            } else {
                OpeningHoursPerDayOfWeek.save(vm.openingHoursPerDayOfWeek, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('businessHoursCalculatorApp:openingHoursPerDayOfWeekUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (error) {
            AlertService.error(error.data.message);
            vm.isSaving = false;
        }


    }
})();
