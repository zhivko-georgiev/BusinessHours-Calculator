(function() {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard-opening-hours-per-specific-date', {
            parent: 'dashboard',
            url: '/opening-hours-per-specific-date',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OpeningHoursPerSpecificDates'
            },
            resolve: {
            }
        })
        .state('dashboard-opening-hours-per-specific-date.new', {
            parent: 'dashboard-opening-hours-per-specific-date',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opening-hours-per-specific-date/opening-hours-per-specific-date-dialog.html',
                    controller: 'OpeningHoursPerSpecificDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: new Date(),
                                openingHours: new Date().getHours() + ":" + (new Date().getMinutes() < 10 ? '0' : '' ) + new Date().getMinutes(),
                                closingHours: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard-opening-hours-per-specific-date', null, { reload: true });
                }, function() {
                    $state.go('dashboard-opening-hours-per-specific-date');
                });
            }]
        })
        .state('dashboard-opening-hours-per-specific-date.edit', {
            parent: 'dashboard-opening-hours-per-specific-date',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opening-hours-per-specific-date/opening-hours-per-specific-date-dialog.html',
                    controller: 'OpeningHoursPerSpecificDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpeningHoursPerSpecificDate', function(OpeningHoursPerSpecificDate) {
                            return OpeningHoursPerSpecificDate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opening-hours-per-specific-date', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dashboard-opening-hours-per-specific-date.delete', {
            parent: 'dashboard-opening-hours-per-specific-date',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opening-hours-per-specific-date/opening-hours-per-specific-date-delete-dialog.html',
                    controller: 'OpeningHoursPerSpecificDateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OpeningHoursPerSpecificDate', function(OpeningHoursPerSpecificDate) {
                            return OpeningHoursPerSpecificDate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard-opening-hours-per-specific-date', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
