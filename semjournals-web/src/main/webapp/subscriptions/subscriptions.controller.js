(function () {
    'use strict';

    angular
        .module('semjournals')
        .controller('SubscriptionsController', SubscriptionsController);

    SubscriptionsController.$inject = ['$rootScope', '$location', 'UserService', 'JournalService', 'SidebarService'];
    function SubscriptionsController($rootScope, $location, UserService, JournalService, SidebarService) {
        var vm = this;

        vm.user = $rootScope.loggedUser;
        vm.subscribedJournals = [];
        vm.Unsubscribe = Unsubscribe;

        initController();

        function initController() {
            if (vm.user.role.name == 'admin') {
                $location.path('/login');
            }

            SidebarService.LoadUserActions();
            loadSubscribedJournals();
        }

        function loadSubscribedJournals() {
            UserService.GetSubscriptions(vm.user.id)
                .then(function (journals) {
                    vm.subscribedJournals = journals.subscriptions;

                    angular.forEach(vm.subscribedJournals, function(obj) {
                        obj['iconId'] = getRandomIconId();
                    });
                });
        }

        function Unsubscribe(journal) {
            JournalService.Unsubscribe(journal.id)
                .then(function () {
                    loadSubscribedJournals();
                });
        }

        function getRandomIconId() {
            var id = Math.floor((Math.random() * 16) + 1);
            return id;
        }
    }

})();
