angular.module('sparkword2vec').controller('Documents',
	function($scope) {
		$scope.documents = [
			{
				link: "www.google.com",
				name: "Documento 1"
			},
			{
				link: "www.google.com",
				name: "Documento 2"
			},
			{
				link: "www.google.com",
				name: "Documento 3"
			},
			{
				link: "www.google.com",
				name: "Documento 4"
			},
		];

		$scope.synonymous = [
			{
				name: "Palavra 1"
			},
			{
				name: "Palavra 2"
			},
			{
				name: "Palavra 3"
			},
			{
				name: "Palavra 4"
			},
		];
	}
);