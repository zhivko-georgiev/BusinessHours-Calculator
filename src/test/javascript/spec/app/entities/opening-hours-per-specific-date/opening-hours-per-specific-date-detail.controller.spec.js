'use strict';

describe('Controller Tests', function() {

    describe('OpeningHoursPerSpecificDate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOpeningHoursPerSpecificDate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOpeningHoursPerSpecificDate = jasmine.createSpy('MockOpeningHoursPerSpecificDate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OpeningHoursPerSpecificDate': MockOpeningHoursPerSpecificDate
            };
            createController = function() {
                $injector.get('$controller')("OpeningHoursPerSpecificDateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'businessHoursCalculatorApp:openingHoursPerSpecificDateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
