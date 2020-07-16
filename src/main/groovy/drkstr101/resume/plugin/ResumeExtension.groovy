/**
 * 
 */
package drkstr101.resume.plugin

import javax.inject.Inject

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional

import drkstr101.resume.plugin.model.Resume

/**
 * @author Aaron R Miller
 *
 */
class ResumeExtension {
	
	private final ResumeModelProvider _provider

	@Input String fullName
	@Input String email
	@Optional @Input String address
	@Optional @Input String phone
	@Nested final NamedDomainObjectContainer<AccomplishmentExtension> accomplishments
	@Nested final NamedDomainObjectContainer<SkillExtension> skills
	@Nested final NamedDomainObjectContainer<EmploymentExtension> employers
	@Nested final NamedDomainObjectContainer<ReferenceExtension> references

	@Inject
	ResumeExtension(ObjectFactory objectFactory) {
		accomplishments = objectFactory.domainObjectContainer(AccomplishmentExtension)
		skills = objectFactory.domainObjectContainer(SkillExtension)
		employers = objectFactory.domainObjectContainer(EmploymentExtension)
		references = objectFactory.domainObjectContainer(ReferenceExtension)
		
		_provider = new ResumeModelProvider(this)
	}
	
	Resume toModel() {
		return _provider.get()
	}


	/**
	 * Constructs a new Calendar instance from a year and 
	 * optional month integer val
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	Calendar calendar(int year, int month = 1) {
		final Calendar cal = Calendar.getInstance()
		cal.set(Calendar.YEAR, year)
		cal.set(Calendar.MONTH, month - 1)
		cal.set(Calendar.DAY_OF_MONTH, 1)
		cal.set(Calendar.HOUR, 0)
		cal.set(Calendar.MINUTE, 0)
		cal.set(Calendar.SECOND, 0)
		cal.set(Calendar.MILLISECOND, 0)

		return cal
	}
}
