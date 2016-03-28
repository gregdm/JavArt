(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('AlbumDetailController', AlbumDetailController);

    AlbumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Album', 'User'];

    function AlbumDetailController($scope, $rootScope, $stateParams, entity, Album, User) {
        var vm = this;
        vm.album = entity;
        vm.load = function (id) {
            Album.get({id: id}, function(result) {
                vm.album = result;
            });
        };
        var unsubscribe = $rootScope.$on('javartApp:albumUpdate', function(event, result) {
            vm.album = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
