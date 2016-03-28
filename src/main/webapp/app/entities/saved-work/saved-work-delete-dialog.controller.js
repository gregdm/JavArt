(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('SavedWorkDeleteController',SavedWorkDeleteController);

    SavedWorkDeleteController.$inject = ['$uibModalInstance', 'entity', 'SavedWork'];

    function SavedWorkDeleteController($uibModalInstance, entity, SavedWork) {
        var vm = this;
        vm.savedWork = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SavedWork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
