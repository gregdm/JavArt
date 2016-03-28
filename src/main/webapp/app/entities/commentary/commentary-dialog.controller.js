(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('CommentaryDialogController', CommentaryDialogController);

    CommentaryDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Commentary', 'User'];

    function CommentaryDialogController ($scope, $stateParams, $uibModalInstance, entity, Commentary, User) {
        var vm = this;
        vm.commentary = entity;
        vm.users = User.query();
        vm.load = function(id) {
            Commentary.get({id : id}, function(result) {
                vm.commentary = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('javartApp:commentaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.commentary.id !== null) {
                Commentary.update(vm.commentary, onSaveSuccess, onSaveError);
            } else {
                Commentary.save(vm.commentary, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
