(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('dashboard', {
            // abstract: true,
            parent: 'app',
            url: '/dashboard',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Business Calculator Dashboard'
            },
            views: {
                'business-hours@': {
                    templateUrl: 'app/entities/business-hours/business-hours.html',
                    controller: 'BusinessHoursController',
                    controllerAs: 'vm'
                },
                'opening-hours-per-day-of-week@': {
                    templateUrl: 'app/entities/opening-hours-per-day-of-week/opening-hours-per-day-of-weeks.html',
                    controller: 'OpeningHoursPerDayOfWeekController',
                    controllerAs: 'vm'
                },
                'opening-hours-per-specific-date@': {
                    templateUrl: 'app/entities/opening-hours-per-specific-date/opening-hours-per-specific-dates.html',
                    controller: 'OpeningHoursPerSpecificDateController',
                    controllerAs: 'vm'
                },
                 'store-closed-per-days-of-week@': {
                    templateUrl: 'app/entities/store-closed-per-days-of-week/store-closed-per-days-of-weeks.html',
                    controller: 'StoreClosedPerDaysOfWeekController',
                    controllerAs: 'vm'
                },
                'store-closed-per-specific-date@': {
                    templateUrl: 'app/entities/store-closed-per-specific-date/store-closed-per-specific-dates.html',
                    controller: 'StoreClosedPerSpecificDateController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();

