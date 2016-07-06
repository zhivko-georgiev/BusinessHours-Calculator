'use strict';

describe('Controller Tests', function() {

    describe('BusinessHours Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBusinessHours;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBusinessHours = jasmine.createSpy('MockBusinessHours');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BusinessHours': MockBusinessHours
            };
            createController = function() {
                $injector.get('$controller')("BusinessHoursDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'businessHoursCalculatorApp:businessHoursUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
