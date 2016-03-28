(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('AlbumDialogController', AlbumDialogController);

    AlbumDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Album', 'User'];

    function AlbumDialogController ($scope, $stateParams, $uibModalInstance, entity, Album, User) {
        var vm = this;
        vm.album = entity;
        vm.users = User.query();
        vm.load = function(id) {
            Album.get({id : id}, function(result) {
                vm.album = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('javartApp:albumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.album.id !== null) {
                Album.update(vm.album, onSaveSuccess, onSaveError);
            } else {
                Album.save(vm.album, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
