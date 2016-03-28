(function() {
    'use strict';

    angular
        .module('javartApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('saved-work', {
            parent: 'entity',
            url: '/saved-work',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SavedWorks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/saved-work/saved-works.html',
                    controller: 'SavedWorkController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('saved-work-detail', {
            parent: 'entity',
            url: '/saved-work/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SavedWork'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/saved-work/saved-work-detail.html',
                    controller: 'SavedWorkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SavedWork', function($stateParams, SavedWork) {
                    return SavedWork.get({id : $stateParams.id});
                }]
            }
        })
        .state('saved-work.new', {
            parent: 'saved-work',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saved-work/saved-work-dialog.html',
                    controller: 'SavedWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                workId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('saved-work', null, { reload: true });
                }, function() {
                    $state.go('saved-work');
                });
            }]
        })
        .state('saved-work.edit', {
            parent: 'saved-work',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saved-work/saved-work-dialog.html',
                    controller: 'SavedWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SavedWork', function(SavedWork) {
                            return SavedWork.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('saved-work', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('saved-work.delete', {
            parent: 'saved-work',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saved-work/saved-work-delete-dialog.html',
                    controller: 'SavedWorkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SavedWork', function(SavedWork) {
                            return SavedWork.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('saved-work', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
