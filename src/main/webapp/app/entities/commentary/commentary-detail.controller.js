(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('CommentaryDetailController', CommentaryDetailController);

    CommentaryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Commentary', 'User'];

    function CommentaryDetailController($scope, $rootScope, $stateParams, entity, Commentary, User) {
        var vm = this;
        vm.commentary = entity;
        vm.load = function (id) {
            Commentary.get({id: id}, function(result) {
                vm.commentary = result;
            });
        };
        var unsubscribe = $rootScope.$on('javartApp:commentaryUpdate', function(event, result) {
            vm.commentary = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
