(function() {
    'use strict';

    angular
        .module('javartApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commentary', {
            parent: 'entity',
            url: '/commentary?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Commentaries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commentary/commentaries.html',
                    controller: 'CommentaryController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('commentary-detail', {
            parent: 'entity',
            url: '/commentary/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Commentary'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commentary/commentary-detail.html',
                    controller: 'CommentaryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Commentary', function($stateParams, Commentary) {
                    return Commentary.get({id : $stateParams.id});
                }]
            }
        })
        .state('commentary.new', {
            parent: 'commentary',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentary/commentary-dialog.html',
                    controller: 'CommentaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                workId: null,
                                validated: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commentary', null, { reload: true });
                }, function() {
                    $state.go('commentary');
                });
            }]
        })
        .state('commentary.edit', {
            parent: 'commentary',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentary/commentary-dialog.html',
                    controller: 'CommentaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commentary', function(Commentary) {
                            return Commentary.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('commentary', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commentary.delete', {
            parent: 'commentary',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentary/commentary-delete-dialog.html',
                    controller: 'CommentaryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commentary', function(Commentary) {
                            return Commentary.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('commentary', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
