package drkstr101.resume.plugin.model

import groovy.transform.Canonical

/**
 * @author Aaron R Miller
 *
 */
@Canonical
class Employment {
	String name
	String type
	String title
	Calendar startDate
	Calendar endDate
	String employer
	String description
	Collection<Accomplishment> accomplishments = []
}
