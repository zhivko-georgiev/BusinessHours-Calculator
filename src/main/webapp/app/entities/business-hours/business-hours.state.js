(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard-business-hours', {
            parent: 'dashboard',
            url: '/business-hours',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BusinessHours'
            },
            views: {
                'business-hours@': {
                    templateUrl: 'app/entities/business-hours/business-hours.html',
                    controller: 'BusinessHoursController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dashboard-business-hours.new', {
            parent: 'dashboard-business-hours',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-hours/business-hours-dialog.html',
                    controller: 'BusinessHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                defaultOpeningHours: new Date().getHours() + ":" + (new Date().getMinutes() < 10 ? '0' : '' ) + new Date().getMinutes(),
                                defaultClosingHours: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard-business-hours', null, { reload: true });
                }, function() {
                    $state.go('dashboard-business-hours');
                });
            }]
        })
        .state('dashboard-business-hours.edit', {
            parent: 'dashboard-business-hours',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-hours/business-hours-dialog.html',
                    controller: 'BusinessHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BusinessHours', function(BusinessHours) {
                            return BusinessHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-business-hours', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dashboard-business-hours.delete', {
            parent: 'dashboard-business-hours',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-hours/business-hours-delete-dialog.html',
                    controller: 'BusinessHoursDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BusinessHours', function(BusinessHours) {
                            return BusinessHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-business-hours', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
