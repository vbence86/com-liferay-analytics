// Predfined scroll levels
// Change this number to use more or less levels 
const scrollDepthLevels = 4; // 25, 50, 75, 100
const levelsReached = [];

/**
 * Returns a function that triggers a callback with a delay. If the function is invoked
 * before the specified delay has passed, it restarts the timer
 * @param {function} callback
 * @param {number} delay
 */
function postponify(callback, delay) {
	let timer;
	return function() {
		if (timer) clearTimeout(timer);
		timer = setTimeout(callback, delay);
	};
}

/**
 * Returns the current scroll position of the page
 * @return {number}
 */
function getCurrentScrollPosition() {
	return window.pageYOffset || document.documentElement.scrollTop;
}

/**
 * Returns the entire height of the document
 * @return {number}
 */
function getDocumentHeight() {
	const heights = [
		document.body.clientHeight,
		document.documentElement.clientHeight,
		document.documentElement.scrollHeight	
	];
	return Math.max(...heights);
}

/**
 * Processes the current scroll location and calculates the scroll depth level
 * If the client reaches one of the pre-defined levels that is yet unreached
 * it registers an analytics event 
 * @param {object} analytics
 */
function processScrollPosition(analytics) {
	const scrollTop = getCurrentScrollPosition();
	const pageHeight = getDocumentHeight();
	const visibleArea = window.innerHeight;
	const depth = Math.round((scrollTop + visibleArea) / pageHeight * 100);
	const step = Math.floor(100 / scrollDepthLevels);
	const depthLevel = Math.floor(depth / step);

	// Sends every individual scroll levels only once when the client
	// reaches it 
	if (levelsReached.some(val => val === depthLevel)) return;

	levelsReached.push(depthLevel);
	if (depthLevel > 0) {
		sendScrollEvent(analytics, depthLevel * step);	
	}
}

/**
 * Sends scroll event according to the given depth level
 * @param {object} analytics
 * @param {object} scrollDepth
 */
function sendScrollEvent(analytics, scrollDepth) {
	const props = {
		scrollDepth
	};
	analytics.send('depthReached', 'scrolling', props);
}

/**
 * Plugin function that registers listener against scroll event
 * @param {object} analytics
 */
function scrolling(analytics) {

	// The callback is going to be registered against the universal scroll
	// event, but it must be excecuted only when the scroll settles
	// for over 1500 milliseconds.
	const process = processScrollPosition.bind(null, analytics);
	const callback = postponify(process, 1500);
	document.addEventListener('scroll', callback);

	// In case of SPA
	window.addEventListener('load', () => levelsReached.splice(0, levelsReached.length))

};

export { scrolling };
export default scrolling;
