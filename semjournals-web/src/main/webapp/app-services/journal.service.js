(function () {
    'use strict';

    angular
        .module('semjournals')
        .factory('JournalService', JournalService);

    JournalService.$inject = ['$http'];
    function JournalService($http) {
        var base = 'https://localhost:8443/v1/journals/';
        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;
        service.Activate = Activate;
        service.Deactivate = Deactivate;

        return service;

        function GetAll() {
            return $http.get(base).then(handleSuccess, handleError('Error getting all journals'));
        }

        function GetById(id) {
            return $http.get(base + id).then(handleSuccess, handleError('Error getting journal by id'));
        }

        function Create(journal) {
            return $http.post(base, journal).then(handleSuccess, handleError('Error creating journal'));
        }

        function Update(journal) {
            return $http.put(base + journal.id, journal).then(handleSuccess, handleError('Error updating journal'));
        }

        function Delete(id) {
            return $http.delete(base + id).then(handleSuccess, handleError('Error deleting journal'));
        }

        function Activate(id) {
            return $http.put(base + id + '/active').then(handleSuccess, handleError('Error setting journal as active'));
        }

        function Deactivate(id) {
            return $http.delete(base + id + '/active').then(handleSuccess, handleError('Error setting journal as inactive'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();