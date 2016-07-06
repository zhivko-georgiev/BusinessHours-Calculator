'use strict';

describe('Controller Tests', function() {

    describe('StoreClosedPerSpecificDate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStoreClosedPerSpecificDate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStoreClosedPerSpecificDate = jasmine.createSpy('MockStoreClosedPerSpecificDate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StoreClosedPerSpecificDate': MockStoreClosedPerSpecificDate
            };
            createController = function() {
                $injector.get('$controller')("StoreClosedPerSpecificDateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'businessHoursCalculatorApp:storeClosedPerSpecificDateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
