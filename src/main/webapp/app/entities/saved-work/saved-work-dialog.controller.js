(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('SavedWorkDialogController', SavedWorkDialogController);

    SavedWorkDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SavedWork', 'User', 'Album'];

    function SavedWorkDialogController ($scope, $stateParams, $uibModalInstance, entity, SavedWork, User, Album) {
        var vm = this;
        vm.savedWork = entity;
        vm.users = User.query();
        vm.albums = Album.query();
        vm.load = function(id) {
            SavedWork.get({id : id}, function(result) {
                vm.savedWork = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('javartApp:savedWorkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.savedWork.id !== null) {
                SavedWork.update(vm.savedWork, onSaveSuccess, onSaveError);
            } else {
                SavedWork.save(vm.savedWork, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
