angular.module('sparkword2vec').controller('Word2Vec',
	function($scope, $http) {
		$scope.searchWord = '';
		$scope.suggestionText = '';
		$scope.isSuggestionVisible = false;
		$scope.documents = [];
		$scope.mensage = '';

		$scope.search = () => {
			if ($scope.searchWord) {
				$http.post('/search_word', {searchWord: $scope.searchWord})
					.then((response) => {
						if (response.data.questions.length) {
							$scope.documents = response.data.questions;
						} else {
							$scope.isSuggestionVisible = true;
						}
					})
					.catch((error) => {
						$scope.mensage = error.mensage;
					});
			} else {
				$scope.mensage = 'digite alguma palavra chave';
			}
		}

		$scope.sendSuggestion = () => {
			const suggestionText = $scope.suggestionText.trim();
			if (!$scope.suggestionText) {
				$scope.mensage = `digite um contexto para a palavra ${$scope.searchWord}.`;
			} else if (!$scope.suggestionText.includes($scope.searchWord)) {
				$scope.mensage = `O contexto deve incluir a palavra ${$scope.searchWord}.`;
			} else{
				$scope.mensage = '';
				$http.post('/suggestion', {suggestionText: $scope.suggestionText})
					.then((response) => {
						$scope.isSuggestionVisible = false;
					})
					.catch((error) => {
						$scope.isSuggestionVisible = false;
					});
			}
		}

		$scope.cancelSuggestion = () => {
			$scope.isSuggestionVisible = false;
			$scope.mensage = '';
		}
	}
);