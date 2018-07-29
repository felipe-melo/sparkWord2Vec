angular.module('sparkword2vec', ['ngRoute', 'ngResource', 'angular-d3-word-cloud'])
	.config(function($routeProvider) {
		$routeProvider.when('/index', {
			templateUrl: 'partials/cloudWords.html',
			controller: 'CloudWords'
		}).otherwise({
            redirectTo: '/index'
        });
	});