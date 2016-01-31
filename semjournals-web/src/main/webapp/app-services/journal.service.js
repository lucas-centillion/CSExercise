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
        service.Upload = Upload;
        service.Update = Update;
        service.Delete = Delete;
        service.Activate = Activate;
        service.Deactivate = Deactivate;
        service.GetSubscribers = GetSubscribers;
        service.Subscribe = Subscribe;
        service.Unsubscribe = Unsubscribe;

        return service;

        function GetAll() {
            return $http.get(base).then(handleSuccess, handleError('Error getting all journals'));
        }

        function GetById(id) {
            return $http.get(base + id).then(handleSuccess, handleError('Error getting journal by id'));
        }

        function Upload(name, formData) {
            return $http.post(base + "upload/" + name, formData, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).then(handleSuccess, handleError('Error uploading journal'));
        }

        function Update(journal, callback) {
            return $http.put(base + journal.id, journal).then(callback, callback);
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

        function GetSubscribers(id) {
            return $http.get(base + id + '/subscribers').then(handleSuccess, handleError('Error getting all journals'));
        }

        function Subscribe(id) {
            return $http.put(base + id + '/subscribers').then(handleSuccess, handleError('Error setting journal as active'));
        }

        function Unsubscribe(id) {
            return $http.delete(base + id + '/subscribers').then(handleSuccess, handleError('Error setting journal as inactive'));
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