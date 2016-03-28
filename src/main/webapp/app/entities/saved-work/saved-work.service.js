(function() {
    'use strict';
    angular
        .module('javartApp')
        .factory('SavedWork', SavedWork);

    SavedWork.$inject = ['$resource'];

    function SavedWork ($resource) {
        var resourceUrl =  'api/saved-works/:id';

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
