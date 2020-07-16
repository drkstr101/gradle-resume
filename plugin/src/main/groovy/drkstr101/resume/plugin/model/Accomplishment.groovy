/**
 * 
 */
package drkstr101.resume.plugin.model

import groovy.transform.Canonical

/**
 * @author Aaron R Miller
 *
 */
@Canonical
class Accomplishment {
	String name
	AccomplishmentType type
	Calendar startDate
	Calendar endDate
	String description
	Collection<Reference> references = []
	Collection<Skill> skills = []
}
