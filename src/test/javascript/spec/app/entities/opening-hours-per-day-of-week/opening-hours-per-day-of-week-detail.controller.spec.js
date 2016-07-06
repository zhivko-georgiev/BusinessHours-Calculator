'use strict';

describe('Controller Tests', function() {

    describe('OpeningHoursPerDayOfWeek Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOpeningHoursPerDayOfWeek;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOpeningHoursPerDayOfWeek = jasmine.createSpy('MockOpeningHoursPerDayOfWeek');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OpeningHoursPerDayOfWeek': MockOpeningHoursPerDayOfWeek
            };
            createController = function() {
                $injector.get('$controller')("OpeningHoursPerDayOfWeekDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'businessHoursCalculatorApp:openingHoursPerDayOfWeekUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
