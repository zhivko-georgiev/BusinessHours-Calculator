(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('OpeningHoursPerSpecificDateController', OpeningHoursPerSpecificDateController);

    OpeningHoursPerSpecificDateController.$inject = ['$scope', '$state', 'OpeningHoursPerSpecificDate'];

    function OpeningHoursPerSpecificDateController ($scope, $state, OpeningHoursPerSpecificDate) {
        var vm = this;
        
        vm.openingHoursPerSpecificDates = [];

        loadAll();

        function loadAll() {
            OpeningHoursPerSpecificDate.query(function(result) {
                vm.openingHoursPerSpecificDates = result;
            });
        }
    }
})();
