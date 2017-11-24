import LCSClient from '../LCSClient';

const ATTR_KEY = 'property';
const ATTR_VALUE = 'content';

const openGraphTagPatterns = [
	/^og:.*/,
	/^music:/,
	/^video:/,
	/^article:/,
	/^book:/,
	/^profile:/,
	/^fb:/
];

/**
 * Determines whether the given tag is a valid OpenGraph meta tag
 * @return {boolean}
 */
function isOpenGraphMetaTag(elm) {
	if (!elm || !elm.getAttribute) return false;
	
	const property = elm.getAttribute(ATTR_KEY);
	if (!property) return false;

	return openGraphTagPatterns.some(regExp => property.match(regExp));
}

/**
 * Collects all OpenGraph meta information from the <head> section
 * @return {object} key-value collection of open graph tags
 */
function getOpenGraphMetaData() {
	const metas = [].slice.call(document.querySelectorAll('meta'));
	return metas
		.filter(meta => isOpenGraphMetaTag(meta))
		.reduce((data, meta) => {
			return {
				[meta.getAttribute(ATTR_KEY)]: meta.getAttribute(ATTR_VALUE),
				...data
			};
		}, {});
}


/**
 * middleware function that augments the request with OpenGraph informations
 * @param {object} req - request object to alter
 * @return {object} the updated request object
 */
function openGraph(req) {
	req.context = {
		...getOpenGraphMetaData(),
		...req.context,
	};
	return req;
}

export { openGraph };
export default openGraph;
