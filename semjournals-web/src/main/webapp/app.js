(function () {
    angular
        .module('semjournals', ['ngRoute', 'ngCookies', 'ngMaterial'])
        .config(config)
        .run(run);

    'use strict';

    config.$inject = ['$routeProvider', '$mdIconProvider', '$locationProvider'];
    function config($routeProvider, $mdIconProvider, $locationProvider) {
        $routeProvider
            .when('/users', {
                controller: 'UsersController',
                templateUrl: 'users/users.view.html',
                controllerAs: 'vm'
            })

            .when('/journals', {
                controller: 'JournalsController',
                templateUrl: 'journals/journals.view.html',
                controllerAs: 'vm'
            })

            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'login/login.view.html',
                controllerAs: 'vm'
            })

            .otherwise({ redirectTo: '/login' });

        $mdIconProvider
            .defaultIconSet("./app-content/svg/avatars.svg", 128)
            .icon("share", "./app-content/svg/share.svg", 24)
            .icon("menu", "./app-content/svg/menu.svg", 24);
    }

    run.$inject = ['$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.loggedUser = $cookieStore.get('loggedUser') || {};
        $rootScope.globals = $cookieStore.get('globals') || {};

        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }
})();
