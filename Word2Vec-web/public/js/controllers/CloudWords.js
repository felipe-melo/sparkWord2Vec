angular.module('sparkword2vec').controller('CloudWords',
	function($scope, $window, $timeout) {
		let originWords = [];
      	const maxWordCount = 1000;
      	$scope.content = 'If you prefer to not use the automatic generator, push a branch named gh-pages to your repository to create a page manually. In addition to supporting regular HTML content, GitHub Pages support Jekyll, a simple, blog aware static site generator GitHub username to generate a link to their profile';
      	$scope.customColor;
      	$scope.generateWords = generateWords;
      	$scope.padding = 8;
      	$scope.editPadding = 8;
      	$scope.useTooltip = false;
      	$scope.useTransition = true;
      	$scope.wordClicked = wordClicked;
      	$scope.words = [];
      	$scope.random = random;
      	generateWords();
      	angular.element($window).bind('resize', resizeWordsCloud);
      /**
       * generate words base on some content by split(/\s+/g) and sort descending
       */
     	function generateWords() {
        	originWords = $scope.content.split(/\s+/g);
         	originWords = originWords.map(function(word) {
	            return {
	               	text: word,
	               	count: Math.floor(Math.random() * maxWordCount)
	            };
         	}).sort(function(a, b) {
            	return b.count - a.count;
         	});
         	resizeWordsCloud();
      	}
      /**
       * adjust words size base on width
       */
      	function resizeWordsCloud() {
        	$timeout(function() {
	            const element = document.getElementById('wordsCloud');
	            const height = $window.innerHeight * 0.75;
	            element.style.height = height + 'px';
	            const width = element.getBoundingClientRect().width;
	            const maxCount = originWords[0].count;
	            const minCount = originWords[originWords.length - 1].count;
	            const maxWordSize = width * 0.15;
	            const minWordSize = maxWordSize / 5;
	            const spread = maxCount - minCount;
	            if (spread <= 0) spread = 1;
	            const step = (maxWordSize - minWordSize) / spread;
	            $scope.words = originWords.map(function(word) {
	               	return {
	                  	text: word.text,
	                  	size: Math.round(maxWordSize - ((maxCount - word.count) * step)),
	                  	color: $scope.customColor,
	                  	tooltipText: word.text + ' tooltip'
	            	};
	            });
	            $scope.width = width;
	            $scope.height = height;
	            $scope.padding = $scope.editPadding;
	            $scope.rotate = $scope.editRotate;
	        });
      	}

      	function random() {
        	return 0.4; // a constant value here will ensure the word position is fixed upon each page refresh.
      	}

      	function wordClicked(word) {
         	alert('text: ' + word.text + ',size: ' + word.size);
		}
	}
);