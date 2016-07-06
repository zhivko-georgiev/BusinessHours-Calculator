'use strict';

describe('Controller Tests', function() {

    describe('BusinessHoursCalculator Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBusinessHoursCalculator;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBusinessHoursCalculator = jasmine.createSpy('MockBusinessHoursCalculator');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BusinessHoursCalculator': MockBusinessHoursCalculator
            };
            createController = function() {
                $injector.get('$controller')("BusinessHoursCalculatorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'businessHoursCalculatorApp:businessHoursCalculatorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
