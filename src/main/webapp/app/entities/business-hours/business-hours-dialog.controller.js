(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('BusinessHoursDialogController', BusinessHoursDialogController);

    BusinessHoursDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BusinessHours'];

    function BusinessHoursDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BusinessHours) {
        var vm = this;

        vm.businessHours = entity;
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
            if (vm.businessHours.id !== null) {
                BusinessHours.update(vm.businessHours, onSaveSuccess, onSaveError);
            } else {
                BusinessHours.save(vm.businessHours, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('businessHoursCalculatorApp:businessHoursUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
