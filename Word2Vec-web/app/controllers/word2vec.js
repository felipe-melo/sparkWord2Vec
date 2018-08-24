var request = require('request');

module.exports = () => {
	const controller = {};
	
	controller.searchWord = (req, res) => {
		request.post(`http://localhost:8001/synonyms?word=${req.body.searchWord}`, {},
		    (error, response, body) => {
		        if (!error && response.statusCode == 200) {
		            console.log(body);
		        } else {
		        	console.log(error);
		        }
		    }
		);
		/*if (req.body.searchWord === 'teste1') {
			res.json({
				questions: [
					{
						link: "http://www.google.com",
						name: "Documento 1"
					},
					{
						link: "http://www.google.com",
						name: "Documento 2"
					},
					{
						link: "http://www.google.com",
						name: "Documento 3"
					},
					{
						link: "http://www.google.com",
						name: "Documento 4"
					},
				]
			});
		} else {
			res.json({questions: []});
		}*/
	};

	controller.suggestion = (req, res) => {
		console.log(req.body);
		res.status(200).end();
	};

	return controller;
}