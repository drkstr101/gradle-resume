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
class Skill {
	String name
	String label
	Skill parent
	Collection<Skill> children = []	
	
	Skill rootNode() {
		if(!parent) return this
		return parent.rootNode()
	}
}
