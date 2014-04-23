'use strict';

var chat =
  angular.module(
    'chat',
    [
      'chat.services',
      'chat.controllers',
      'ngRoute'
    ]
  );

chat.config(['$routeProvider', function($routeProvider) {
  $routeProvider
    .when('/', { templateUrl: 'partials/home.html', controller: 'HomeCtrl' })
    .otherwise({ redirectTo: '/' });
}]);
