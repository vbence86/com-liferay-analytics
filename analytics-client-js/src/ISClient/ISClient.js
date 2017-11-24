require('es6-promise').polyfill();
require('isomorphic-fetch');

// Default Middlewares
import defaultMiddlewares from './middlewares/defaults';
const middlewares = defaultMiddlewares;

const IS_ENDPOINT =
	'http://pulpo-engine-contacts-pre.eu-west-1.elasticbeanstalk.com/my-project-id/identity';

/**
 * Returns a resolved or rejected promise as per the response status
 * @param {object} The Analytics instance from which the data is extracted
 * @return {object} Promise object representing the result of the operation
 */
function getUserId(analytics) {
	const request = getISRequest(analytics);
	const url = analytics.getIdentiyServiceUrl() || IS_ENDPOINT;
	return fetch(IS_ENDPOINT, request)
		.then(validate)
		.then(jsonify)
		.then(resp => resp.userId);
}

/**
 * Returns a Request object for the Identity Service
 * includin the batched event objects
 * @param {object} The Analytics instance from which the data is extracted
 * @return {object} Request
 */
function getISRequest(analytics) {
	const headers = new Headers();
	const body = JSON.stringify(getRequestBody(analytics));

	headers.append('Content-Type', 'application/json');

	return {
		method: 'POST',
		mode: 'cors',
		credentials: 'same-origin',
		cache: 'default',
		headers,
		body,
	};
}

/**
 * Returns the formatted version of the analytics data that complies to the
 * predefined request specification of the IS endpoint
 * @param {object} The Analytics instance from which the data is extracted
 * @return {object} object literal
 */
function getRequestBody(analytics) {
	const requestBody = {};
	return middlewares.reduce(
		(request, middleware) => middleware(request, analytics),
		requestBody
	);
}

/**
 * returns the Response object or a rejected Promise based on the
 * HTTP Response Code of the Response object
 * @param {object} resp
 * @return {object} Promise
 */
function validate(resp) {
	if (resp.ok) {
		return resp;
	} else {
		return new Promise((resolve, reject) => reject(resp));
	}
}

/**
 * Parses the response in order to create a Javascript object from it
 * @param  {Response} resp native Response object coming from the Fetch API
 * @return {Promise} Promise object representing the result of the operation]
 */
function jsonify(resp){
  if (!resp || !resp.json) throw 'Invalid Response object!';
  return resp.json();
}

/**
 * Adds middleware function to provide ability to transform the request
 * that is sent to LCS endpoint
 * @param {function} middleware function to alter request
 * @example
 * ISClient.use((req, analytics) => {
 *   req.firstEvent = analytics.getEvents()[0];
 *   req.myMetaInfo = 'myMetaInfo';
 *   return req;
 * });
 */
function use(middleware) {
	middlewares.push(middleware);
}

// expose the API of the Client
const ISClient = {
	use,
	send,
};

export {ISClient};
export default ISClient;