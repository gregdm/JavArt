(function() {
    'use strict';

    angular
        .module('javartApp')
        .controller('CommentaryDeleteController',CommentaryDeleteController);

    CommentaryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Commentary'];

    function CommentaryDeleteController($uibModalInstance, entity, Commentary) {
        var vm = this;
        vm.commentary = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Commentary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
