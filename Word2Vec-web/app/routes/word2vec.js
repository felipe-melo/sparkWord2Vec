module.exports = (app) => {
	const controller = app.controllers.word2vec;

	app.route('/cloudWords')
		.get(controller.cloudWords)
};