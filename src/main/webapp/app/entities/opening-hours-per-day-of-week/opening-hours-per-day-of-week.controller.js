(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .controller('OpeningHoursPerDayOfWeekController', OpeningHoursPerDayOfWeekController);

    OpeningHoursPerDayOfWeekController.$inject = ['$scope', '$state', 'OpeningHoursPerDayOfWeek'];

    function OpeningHoursPerDayOfWeekController ($scope, $state, OpeningHoursPerDayOfWeek) {
        var vm = this;
        
        vm.openingHoursPerDayOfWeeks = [];

        loadAll();

        function loadAll() {
            OpeningHoursPerDayOfWeek.query(function(result) {
                vm.openingHoursPerDayOfWeeks = result;
            });
        }
    }
})();
