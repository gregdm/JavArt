(function() {
    'use strict';
    angular
        .module('javartApp')
        .factory('Album', Album);

    Album.$inject = ['$resource'];

    function Album ($resource) {
        var resourceUrl =  'api/albums/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
