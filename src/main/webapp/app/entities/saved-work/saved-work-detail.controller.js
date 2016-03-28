(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('SavedWorkDetailController', SavedWorkDetailController);

    SavedWorkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SavedWork', 'User', 'Album'];

    function SavedWorkDetailController($scope, $rootScope, $stateParams, entity, SavedWork, User, Album) {
        var vm = this;
        vm.savedWork = entity;
        vm.load = function (id) {
            SavedWork.get({id: id}, function(result) {
                vm.savedWork = result;
            });
        };
        var unsubscribe = $rootScope.$on('javartApp:savedWorkUpdate', function(event, result) {
            vm.savedWork = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
