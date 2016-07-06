(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard-store-closed-per-specific-date', {
            parent: 'dashboard',
            url: '/store-closed-per-specific-date',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StoreClosedPerSpecificDates'
            },
            resolve: {
            }
        })
        .state('dashboard-store-closed-per-specific-date.new', {
            parent: 'dashboard-store-closed-per-specific-date',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-closed-per-specific-date/store-closed-per-specific-date-dialog.html',
                    controller: 'StoreClosedPerSpecificDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: new Date(),
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard-store-closed-per-specific-date', null, { reload: true });
                }, function() {
                    $state.go('dashboard-store-closed-per-specific-date');
                });
            }]
        })
        .state('dashboard-store-closed-per-specific-date.edit', {
            parent: 'dashboard-store-closed-per-specific-date',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-closed-per-specific-date/store-closed-per-specific-date-dialog.html',
                    controller: 'StoreClosedPerSpecificDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StoreClosedPerSpecificDate', function(StoreClosedPerSpecificDate) {
                            return StoreClosedPerSpecificDate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-store-closed-per-specific-date', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dashboard-store-closed-per-specific-date.delete', {
            parent: 'dashboard-store-closed-per-specific-date',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-closed-per-specific-date/store-closed-per-specific-date-delete-dialog.html',
                    controller: 'StoreClosedPerSpecificDateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StoreClosedPerSpecificDate', function(StoreClosedPerSpecificDate) {
                            return StoreClosedPerSpecificDate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard--store-closed-per-specific-date', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
