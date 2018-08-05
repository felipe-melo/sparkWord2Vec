angular.module('sparkword2vec', ['ngRoute', 'ngResource', 'ui.router', 'angular-d3-word-cloud'])
	.config(function($routeProvider, $stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise('/index');

		$stateProvider
			.state('index', {
				url: '/index',
				views: {
					'': {
						templateUrl: 'partials/header.html'
					},
					'documents@index': {
						templateUrl: 'partials/documents.html',
						controller: 'Documents'
					},
					'cloudWords@index': {
						templateUrl: 'partials/cloudWords.html',
						controller: 'CloudWords'
					}
				}
			});
	});