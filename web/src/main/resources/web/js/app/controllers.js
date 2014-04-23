'use strict';

var chatControllers =
  angular.module(
    'chat.controllers',
    [
      'chat.services'
    ]
  );

chatControllers.controller('HomeCtrl', ['$scope', 'Message', function($scope, Message) {

  $scope.messages = [];

  $scope.getMessages = function() {
    var messages = Message.query(function() {
      $scope.messages = messages.concat($scope.messages);
      $scope.getMessages();
    });
  };

  $scope.message = new Message({ 'username': '' });

  $scope.sendMessage = function() {
    $scope.message.$save();
    $scope.message = new Message({ 'username': '' });
  };
}]);
