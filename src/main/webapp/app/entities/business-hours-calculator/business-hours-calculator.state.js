(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard-business-hours-calculator', {
            parent: 'app',
            url: '/business-hours-calculator',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BusinessHoursCalculators'
            },
            views: {
                'business-hours-calculator@': {
                    templateUrl: 'app/entities/business-hours-calculator/business-hours-calculators.html',
                    controller: 'BusinessHoursCalculatorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dashboard-business-hours-calculator.new', {
            parent: 'dashboard-business-hours-calculator',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-hours-calculator/business-hours-calculator-dialog.html',
                    controller: 'BusinessHoursCalculatorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                timeInterval: null,
                                startingDateTime: null,
                                expectedPickupTime: null,
                                id: null,
                                actualBusinessHours: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard-business-hours-calculator', null, { reload: true });
                }, function() {
                    $state.go('dashboard-business-hours-calculator');
                });
            }]
        })
        .state('dashboard-business-hours-calculator.edit', {
            parent: 'dashboard-business-hours-calculator',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-hours-calculator/business-hours-calculator-dialog.html',
                    controller: 'BusinessHoursCalculatorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BusinessHoursCalculator', function(BusinessHoursCalculator) {
                            return BusinessHoursCalculator.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-business-hours-calculator', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dashboard-business-hours-calculator.delete', {
            parent: 'dashboard-business-hours-calculator',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-hours-calculator/business-hours-calculator-delete-dialog.html',
                    controller: 'BusinessHoursCalculatorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BusinessHoursCalculator', function(BusinessHoursCalculator) {
                            return BusinessHoursCalculator.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-business-hours-calculator', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
