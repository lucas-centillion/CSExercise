(function () {
    'use strict';

    angular
        .module('semjournals')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$rootScope', '$scope','$location', '$mdSidenav', 'SidebarService'];
    function SidebarController($rootScope, $scope, $location, $mdSidenav, SidebarService) {
        var vm = this;

        vm.actions = []
        vm.selectAction = selectAction;
        vm.toggleMenu = toggleMenu;

        initController();

        function initController() {
            SidebarService.LoadLoginActions();
        }

        function toggleMenu() {
            $mdSidenav('actionsMenu').toggle();
        }

        function selectAction(action) {
            $location.path('/' + action.name);
        }

        $scope.$watch(function () {
                return SidebarService.actions;
            },
            function(actions) {
                vm.actions = actions;
            },
        true);
    }

})();
