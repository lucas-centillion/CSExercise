(function () {
    'use strict';

    angular
        .module('semjournals')
        .controller('JournalsController', JournalsController);

    JournalsController.$inject = ['$rootScope', 'JournalService', 'SidebarService'];
    function JournalsController($rootScope, JournalService, SidebarService) {
        var vm = this;

        vm.user = $rootScope.loggedUser;
        vm.allJournals = [];
        vm.deleteJournal = deleteJournal;
        vm.toggleActivation = toggleActivation;
        vm.toggleSubscription = toggleSubscription;

        initController();

        function initController() {
            if (vm.user.role.name == 'user') {
                SidebarService.LoadUserActions();
            } else {
                SidebarService.LoadAdminActions();
            }
            loadAllJournals();
        }

        function loadAllJournals() {
            JournalService.GetAll()
                .then(function (journals) {
                    vm.allJournals = journals;

                    angular.forEach(vm.allJournals, function(obj) {
                        obj['iconId'] = getRandomIconId();
                    });
                });
        }

        function deleteJournal(journal) {
            JournalService.Delete(journal.id)
                .then(function () {
                    loadAllJournals();
                });
        }

        function toggleActivation(journal) {
            if (journal.active) {
                JournalService.Deactivate(journal.id)
                    .then(function () {
                        loadAllJournals();
                    });
            } else {
                JournalService.Activate(journal.id)
                    .then(function () {
                        loadAllJournals();
                    });
            }
        }

        function toggleSubscription(journal) {

        }

        function getRandomIconId() {
            var id = Math.floor((Math.random() * 16) + 1);
            return id;
        }
    }

})();
