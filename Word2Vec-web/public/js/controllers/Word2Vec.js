angular.module('sparkword2vec').controller('Word2Vec',
	function($scope, $window, $http, Word2VecService) {
		$scope.searchWord = '';
		$scope.suggestionText = '';
		$scope.isSuggestionVisible = false;
		$scope.posts = [];
		$scope.mensage = '';

		$scope.search = () => {
			if ($scope.searchWord) {
				$http.post('/search_word', {searchWord: convertSearch($scope.searchWord)})
					.then((response) => {
						if (response.data.posts.length) {
							$scope.posts = response.data.posts;
							Word2VecService.words = response.data.words;
						} else {
							$scope.posts = [];
							Word2VecService.words = []
							$scope.isSuggestionVisible = true;
						}
						Word2VecService.reload();
					})
					.catch((error) => {
						$scope.mensage = error.mensage;
					}); 
			} else {
				$scope.mensage = 'digite alguma palavra chave';
			}
		}

		const convertSearch = (searchField) => {
			let aux = '';

			if (searchField.includes('-')) {
				searchField.split('-').forEach(subset => {
					aux += subset.trim().split(' ').join('_') + '-';
				});
				aux = aux.substring(0, aux.length-1);
			} else {
				aux = searchField.split(' ').join('_');
			}
			console.log(aux);
			return aux;
		};

		$scope.sendSuggestion = () => {
			const suggestionText = $scope.suggestionText.trim();
			if (!$scope.suggestionText) {
				$scope.mensage = `digite um contexto para a palavra ${$scope.searchWord}.`;
			} else if (!$scope.suggestionText.includes($scope.searchWord)) {
				$scope.mensage = `O contexto deve incluir a palavra ${$scope.searchWord}.`;
			} else if (($scope.suggestionText.split($scope.searchWord).length -1 < 5)) {
				$scope.mensage = `O contexto deve incluir a palavra ${$scope.searchWord} pelo menos 5 vezes.`;
			} else{
				$scope.mensage = '';
				$http.post('/suggestion', {suggestionText: $scope.suggestionText})
					.then((response) => {
						$scope.isSuggestionVisible = false;
						$scope.suggestionText = "";
						alert("Sugestão recebida com sucesso!");
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

		$scope.onSearchChange = (value) => {
			$scope.isSuggestionVisible = false;
			$scope.mensage = '';
		}
	}
);