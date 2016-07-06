(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard-store-closed-per-days-of-week', {
            parent: 'dashboard',
            url: '/store-closed-per-days-of-week',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StoreClosedPerDaysOfWeeks'
            },
            resolve: {
            }
        })
        .state('dashboard-store-closed-per-days-of-week.new', {
            parent: 'dashboard-store-closed-per-days-of-week',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-closed-per-days-of-week/store-closed-per-days-of-week-dialog.html',
                    controller: 'StoreClosedPerDaysOfWeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dayOfWeek: "MONDAY",
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard-store-closed-per-days-of-week', null, { reload: true });
                }, function() {
                    $state.go('dashboard-store-closed-per-days-of-week');
                });
            }]
        })
        .state('dashboard-store-closed-per-days-of-week.edit', {
            parent: 'dashboard-store-closed-per-days-of-week',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-closed-per-days-of-week/store-closed-per-days-of-week-dialog.html',
                    controller: 'StoreClosedPerDaysOfWeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StoreClosedPerDaysOfWeek', function(StoreClosedPerDaysOfWeek) {
                            return StoreClosedPerDaysOfWeek.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-store-closed-per-days-of-week', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dashboard-store-closed-per-days-of-week.delete', {
            parent: 'dashboard-store-closed-per-days-of-week',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-closed-per-days-of-week/store-closed-per-days-of-week-delete-dialog.html',
                    controller: 'StoreClosedPerDaysOfWeekDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StoreClosedPerDaysOfWeek', function(StoreClosedPerDaysOfWeek) {
                            return StoreClosedPerDaysOfWeek.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-store-closed-per-days-of-week', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
