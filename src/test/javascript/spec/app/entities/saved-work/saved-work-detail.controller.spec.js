'use strict';

describe('Controller Tests', function() {

    describe('SavedWork Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSavedWork, MockUser, MockAlbum;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSavedWork = jasmine.createSpy('MockSavedWork');
            MockUser = jasmine.createSpy('MockUser');
            MockAlbum = jasmine.createSpy('MockAlbum');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SavedWork': MockSavedWork,
                'User': MockUser,
                'Album': MockAlbum
            };
            createController = function() {
                $injector.get('$controller')("SavedWorkDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'javartApp:savedWorkUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
