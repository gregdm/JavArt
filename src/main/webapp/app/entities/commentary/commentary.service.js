(function() {
    'use strict';
    angular
        .module('javartApp')
        .factory('Commentary', Commentary);

    Commentary.$inject = ['$resource'];

    function Commentary ($resource) {
        var resourceUrl =  'api/commentaries/:id';

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
