'use strict';

var chatServices =
  angular.module(
    'chat.services',
    [
      'ngResource'
    ]
  );

chatServices.factory(
  'Message',
  function($resource) {
    return $resource('api/messages');
  });
