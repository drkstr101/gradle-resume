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
class Resume {
	String fullName
	String email
	String address
	String phone
	Map<String, Skill> skillsByName = [:]
	Collection<Accomplishment> accomplishments = []
	Collection<Employment> employers = []
	Collection<Reference> references = []
	
	Reference referenceNamed(String name) {
		def model = this.references.find { it.name == name }
		if(!model) throw new RuntimeException("Reference ${name} does not exist")
		
		return model
	}
	
	Accomplishment accomplishmentNamed(String name) {
		def model = this.accomplishments.find { it.name == name }
		if(!model) throw new RuntimeException("Accomplishment ${name} does not exist")
		
		return model
	}
	
	Employment employmentNamed(String name) {
		def model = this.employers.find { it.name == name }
		if(!model) throw new RuntimeException("Reference ${name} does not exist")
		
		return model
	}
}
