(function () {
    'use strict';

    angular
        .module('semjournals')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$window', '$location', 'AuthenticationService', 'FlashService', 'SidebarService'];
    function LoginController($window, $location, AuthenticationService, FlashService, SidebarService) {
        var vm = this;

        vm.login = login;
        vm.username = 'mario@gmail.com';
        vm.password = 'abc12345';

        (function initController() {
            AuthenticationService.ClearCredentials();
            SidebarService.LoadLoginActions();
        })();

        function login() {
            vm.dataLoading = true;

            AuthenticationService.SetCredentials(vm.username, vm.password);

            AuthenticationService.Login(function (response) {
                if (response.status == 200) {
                    AuthenticationService.SetCredentials(vm.username, vm.password);
                    AuthenticationService.SetLoggedUser(response.data);

                    if (response.data.role.name == 'user') {
                        $location.path('/journals');
                    } else {
                        $location.path('/users');
                    }
                } else {
                    AuthenticationService.ClearCredentials();
                    FlashService.Error("Invalid e-mail / password");
                    vm.dataLoading = false;
                }
            });
        };
    }

})();
