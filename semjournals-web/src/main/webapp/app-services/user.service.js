(function () {
    'use strict';

    angular
        .module('semjournals')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var base = 'https://localhost:8443/v1/accounts/';
        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Self = Self;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;
        service.ActivateAccount = ActivateAccount;
        service.DeactivateAccount = DeactivateAccount;
        service.AddAdminRole = AddAdminRole;
        service.RemoveAdminRole = RemoveAdminRole;
        service.GetSubscriptions = GetSubscriptions;

        return service;

        function Self(success, failure) {
            return $http.get(base + 'self').then(success, failure);
        }

        function GetAll() {
            return $http.get(base).then(handleSuccess, handleError('Error getting all users'));
        }

        function GetById(id) {
            return $http.get(base + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function Create(user) {
            return $http.post(base, user).then(handleSuccess, handleError('Error creating user'));
        }

        function Update(user) {
            return $http.put(base + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Delete(id) {
            return $http.delete(base + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function ActivateAccount(id) {
            return $http.put(base + id + '/active').then(handleSuccess, handleError('Error updating user'));
        }

        function DeactivateAccount(id) {
            return $http.delete(base + id + '/active').then(handleSuccess, handleError('Error deleting user'));
        }

        function AddAdminRole(id) {
            return $http.put(base + id + '/admin').then(handleSuccess, handleError('Error updating user'));
        }

        function RemoveAdminRole(id) {
            return $http.delete(base + id + '/admin').then(handleSuccess, handleError('Error deleting user'));
        }

        function GetSubscriptions(id) {
            return $http.get(base + id + '/subscriptions').then(handleSuccess, handleError('Error getting users subscriptions'));
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