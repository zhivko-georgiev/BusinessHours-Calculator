'use strict';

describe('Controller Tests', function() {

    describe('StoreClosedPerDayOfWeek Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStoreClosedPerDayOfWeek;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStoreClosedPerDayOfWeek = jasmine.createSpy('MockStoreClosedPerDayOfWeek');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StoreClosedPerDayOfWeek': MockStoreClosedPerDayOfWeek
            };
            createController = function() {
                $injector.get('$controller')("StoreClosedPerDayOfWeekDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'businessHoursCalculatorApp:storeClosedPerDayOfWeekUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
