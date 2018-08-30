module.exports = (app) => {
	const controller = app.controllers.word2vec;

	app.route('/search_word')
		.post(controller.searchWord)
	app.route('/suggestion')
		.post(controller.suggestion)
};