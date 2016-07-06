(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard-opening-hours-per-day-of-week', {
            parent: 'dashboard',
            url: '/opening-hours-per-day-of-week',
            resolve: {
            }
        })
        .state('dashboard-opening-hours-per-day-of-week.new', {
            parent: 'dashboard-opening-hours-per-day-of-week',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opening-hours-per-day-of-week/opening-hours-per-day-of-week-dialog.html',
                    controller: 'OpeningHoursPerDayOfWeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dayOfWeek: "MONDAY",
                                openingHours: new Date().getHours() + ":" + (new Date().getMinutes() < 10 ? '0' : '' ) + new Date().getMinutes(),
                                closingHours: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard-opening-hours-per-day-of-week', null, { reload: true });
                }, function() {
                    $state.go('dashboard-opening-hours-per-day-of-week');
                });
            }]
        })
        .state('dashboard-opening-hours-per-day-of-week.edit', {
            parent: 'dashboard-opening-hours-per-day-of-week',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opening-hours-per-day-of-week/opening-hours-per-day-of-week-dialog.html',
                    controller: 'OpeningHoursPerDayOfWeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpeningHoursPerDayOfWeek', function(OpeningHoursPerDayOfWeek) {
                            return OpeningHoursPerDayOfWeek.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-opening-hours-per-day-of-week', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dashboard-opening-hours-per-day-of-week.delete', {
            parent: 'dashboard-opening-hours-per-day-of-week',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opening-hours-per-day-of-week/opening-hours-per-day-of-week-delete-dialog.html',
                    controller: 'OpeningHoursPerDayOfWeekDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OpeningHoursPerDayOfWeek', function(OpeningHoursPerDayOfWeek) {
                            return OpeningHoursPerDayOfWeek.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-opening-hours-per-day-of-week', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
