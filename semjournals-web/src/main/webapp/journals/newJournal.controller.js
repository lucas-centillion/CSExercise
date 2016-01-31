(function () {
    'use strict';

    angular
        .module('semjournals')
        .controller('NewJournalController', NewJournalController)
        .directive('fileModel', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    var model = $parse(attrs.fileModel);
                    var modelSetter = model.assign;

                    element.bind('change', function(){
                        scope.$apply(function(){
                            modelSetter(scope, element[0].files[0]);
                        });
                    });
                }
            };
        }]);

    NewJournalController.$inject = ['$rootScope', '$scope', '$location', 'JournalService', 'SidebarService'];
    function NewJournalController($rootScope, $scope, $location, JournalService, SidebarService) {
        var vm = this;

        vm.user = $rootScope.loggedUser;
        vm.name;
        vm.dataLoading
        vm.uploadJournal = uploadJournal;

        initController();

        function initController() {
            if (vm.user.role.name == 'user') {
                $location.path('/login');
            }

            SidebarService.LoadAdminActions();
        }

        function uploadJournal() {
            vm.dataLoading = true;
            var fd = new FormData();
            fd.append('file', $scope.journalFile);
            JournalService.Upload(vm.name, fd, function (response) {
                if (response.status == 200) {
                    $location.path('/journals');
                } else if (response.status == 409) {
                    FlashService.Error("A journal with this name already exists");
                } else {
                    FlashService.Error("Server error - please try again or report this error");
                }
                vm.dataLoading = false;
            });
        }
    }

})();
