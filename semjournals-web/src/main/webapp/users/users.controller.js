(function () {
    'use strict';

    angular
        .module('semjournals')
        .controller('UsersController', UsersController);

    UsersController.$inject = ['$rootScope', '$location', 'UserService', 'SidebarService'];
    function UsersController($rootScope, $location, UserService, SidebarService) {
        var vm = this;

        vm.user = $rootScope.loggedUser;
        vm.allUsers = [];
        vm.deleteUser = deleteUser;
        vm.toggleActivation = toggleActivation;
        vm.toggleAdminRole = toggleAdminRole;

        initController();

        function initController() {
            if (vm.user.role.name == 'user') {
                $location.path('/login');
            }

            SidebarService.LoadAdminActions();
            loadAllUsers();
        }

        function loadAllUsers() {
            UserService.GetAll()
                .then(function (users) {
                    vm.allUsers = users;

                    angular.forEach(vm.allUsers, function(obj) {
                        obj['iconId'] = getRandomIconId();
                    });
                });
        }

        function deleteUser(user) {
            UserService.Delete(user.id)
                .then(function () {
                    loadAllUsers();
                });
        }

        function toggleActivation(user) {
            if (user.active) {
                UserService.DeactivateAccount(user.id)
                    .then(function () {
                        loadAllUsers();
                    });
            } else {
                UserService.ActivateAccount(user.id)
                    .then(function () {
                        loadAllUsers();
                    });
            }
        }

        function toggleAdminRole(user) {
            if (user.role.name=='user') {
                UserService.AddAdminRole(user.id)
                    .then(function () {
                        loadAllUsers();
                    });
            } else if (user.role.name=='admin') {
                UserService.RemoveAdminRole(user.id)
                    .then(function () {
                        loadAllUsers();
                    });
            }
        }

        function getRandomIconId() {
            var id = Math.floor((Math.random() * 16) + 1);
            return id;
        }
    }

})();
