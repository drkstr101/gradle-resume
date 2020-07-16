/**
 * 
 */
package drkstr101.resume.plugin

import org.gradle.api.internal.provider.AbstractMinimalProvider
import org.gradle.internal.UncheckedException

import drkstr101.resume.plugin.model.Accomplishment
import drkstr101.resume.plugin.model.Employment
import drkstr101.resume.plugin.model.Reference
import drkstr101.resume.plugin.model.Resume
import drkstr101.resume.plugin.model.Skill

/**
 * @author Aaron R Miller
 *
 */
class ResumeModelProvider extends AbstractMinimalProvider<Resume> {

	private final ResumeExtension resume
	private final Map<String, Skill> skillsByName = [:]
	private final Map<String, Reference> referencesByName = [:]
	private final Map<String, Skill> accomplishmentsByName = [:]
	private final Map<String, Employment> employersByName = [:]
	
	private Resume _model


	/**
	 * @param resume
	 */
	public ResumeModelProvider(ResumeExtension resume) {
		super()
		this.resume = resume
	}

	@Override
	public Class<Resume> getType() {
		return Resume.class
	}

	@Override
	protected Value<? extends Resume> calculateOwnValue(ValueConsumer consumer) {
		
		if(_model != null) {
			return Value.of(_model)
		}

		try {
			// println "Caching resume model... "
			
			resume.skills.forEach { skill ->
				walkAndCollectSkills(skillsByName, skill, null)
			}

			def references = resume.references.collect(this.&toReferenceModel)
			def accomplishments = resume.accomplishments.collect(this.&toAccomplishmentModel)
			def employers = resume.employers.collect(this.&toEmploymentModel)

			_model = new Resume(
					fullName: resume.fullName,
					email: resume.email,
					address: resume.address,
					phone: resume.phone,
					skillsByName: skillsByName,
					references: references,
					accomplishments: accomplishments,
					employers: employers
			)


			// return wrapped model value
			return Value.of(_model)
		} catch(Exception e) {
			UncheckedException.throwAsUncheckedException(e)
		}
	}

	protected Map<String, Skill> walkAndCollectSkills(Map<String, Skill> collector, SkillExtension skill, Skill parent = null) {
		def name = skill.name
		def model = new Skill(name: name, label: skill.label, parent: parent)

		collector.put(name, model)
		skill.children.each {
			walkAndCollectSkills(collector, it, model)
		}

		return collector
	}

	protected Reference toReferenceModel(ReferenceExtension reference) {
		def model = new Reference(name: reference.name, description: reference.description)
		referencesByName.put(reference.name, model)
		
		return model
	}

	protected Accomplishment toAccomplishmentModel(AccomplishmentExtension accomplishment) {
		def model = new Accomplishment(
				name: accomplishment.name,
				type: accomplishment.type,
				startDate: accomplishment.startDate,
				endDate: accomplishment.endDate,
				description: accomplishment.description,
				references: referencesByName(accomplishment.references),
				skills: skillsByName(accomplishment.skills)
		)
		accomplishmentsByName.put(accomplishment.name, model)
		
		return model
	}

	protected Employment toEmploymentModel(EmploymentExtension employment) {
		def model = new Employment(name: employment.name,
				type: employment.type,
				title: employment.title,
				startDate: employment.startDate,
				endDate: employment.endDate,
				employer: employment.employer,
				description: employment.description,
				accomplishments: accomplishmentsByName(employment.accomplishments)
		)

		employersByName.put(employment.name, model)
		
		return model
	}

	protected Set<Skill> skillsByName(Set<String> skillNames) {
		return skillNames.collect { String name ->
			if(!skillsByName.containsKey(name)) throw new RuntimeException("Skill ${name} does not exist")
			skillsByName.get(name)
		}
	}

	protected Set<Reference> referencesByName(Set<String> referenceNames) {
		return referenceNames.collect { String name ->
			if(!referencesByName.containsKey(name)) throw new RuntimeException("Reference ${name} does not exist")
			referencesByName.get(name)
		}
	}

	protected Set<Accomplishment> accomplishmentsByName(Set<String> accomplishmentNames) {
		return accomplishmentNames.collect { String name ->
			if(!accomplishmentsByName.containsKey(name)) throw new RuntimeException("Accomplishment ${name} does not exist")
			accomplishmentsByName.get(name)
		}
	}
}
