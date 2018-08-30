const axios = require('axios');

module.exports = () => {
	const controller = {};

	const searchStackoverflow = async (res, words) => {
		let arr = [];

		/*words.forEach (word => {
			arr.push(axios.get(`https://api.stackexchange.com/2.2/search/advanced?order=desc&sort=relevance&q=${word.text}&site=pt.stackoverflow&filter=!9Z(-wu0BT`));
		});*/

		let results = [{data: {items: [{title: 'who cares', link: 'https://fuckyou.com'}]}}]
		/*let results = await axios.all(arr)
			.catch((err) => {
				console.log('Error', err);
			});*/

		if (results.length) {
			const posts = []
			
			results.forEach(result => {
				if (result.data.items.length) {
					posts.push({
						'link': result.data.items[0].link,
						'title': result.data.items[0].title
					});
				}
			});
			res.json({posts, words});
		} else {
			res.json({posts: [], words: []});
		}
	};
	
	controller.searchWord = (req, res) => {
		axios.get(`http://localhost:8001/synonyms?word=${encodeURIComponent(req.body.searchWord)}`)
			.then((response) => {
				if (response.data.words)
					searchStackoverflow(res, response.data.words);
				else
					res.json({posts: [], words: []})
			})
			.catch((error) => {
				console.log('Error', error);
			});
	};

	controller.suggestion = (req, res) => {
		axios.post(`http://localhost:8001/suggestion?suggestionText=${encodeURIComponent(req.body.suggestionText)}`)
			.then((response) => {
				res.json({"mensagem": response.data})
			})
			.catch((error) => {
				console.log('Error - spark', error)
			});
	};

	return controller;
}