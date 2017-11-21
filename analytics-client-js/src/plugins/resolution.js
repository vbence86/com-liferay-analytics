/**
 * Cross-browser solution that returns the width of the client area
 * @return {integer}
 */
function getWidth() { 
	return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
}

/**
 * Cross-browser solution that returns the height of the client area
 * @return {integer}
 */
function getHeight() {
	return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
}

/**
 * Returns the device pixel ratio if accessable
 * @return {number}
 */
function getDevicePixelRatio() {
	return window.devicePixelRatio || 1;
}

/**
 * Registers a custom middleware to alter the event context
 * with the current resolution of the browsers client area
 * @param {object} analytics - Analytics singleton instance  
 */
function resolution(analytics) {
	analytics.registerMiddleware(extendContextWithResolutionData);
}

/**
 * Middleware function to alter the event context with the 
 * browser resolution informations
 */
function extendContextWithResolutionData(req, analytics) {
	req.context = {
		screenWidth: getWidth(),
		screenHeight: getHeight(),
		devicePixelRatio: getDevicePixelRatio(),
		...req.context
	}
	return req;
}

export { resolution };
export default resolution;
