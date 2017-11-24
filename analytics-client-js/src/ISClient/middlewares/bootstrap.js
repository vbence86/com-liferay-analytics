/**
 * Bootstraps the basic message to IS
 * @param {object} req - request object to alter
 * @param {object} analytics - Analytics instance to extract behaviour informations from it
 * @return {object} the updated request object
 */
function bootstrap(req, analytics) {
	const config = analytics.getConfig();

	// Bootstrap the default Request Body
	const requestBody = {
		analyticsKey: config.analyticsKey,
		protocolVersion: '1.0',
	};

	if (config.email) {
		requestBody.email = email;
	}

	return {
		...requestBody,
		...req,
	};
}

export { bootstrap };
export default bootstrap;
