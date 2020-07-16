package drkstr101.resume.plugin

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import drkstr101.resume.plugin.model.AccomplishmentType

/**
 * @author Aaron R Miller
 *
 */
class AccomplishmentExtension implements Named {

	private final String name

	@Input
	String getName() {
		return name
	}

	@Input AccomplishmentType type

	@Optional @Input String description

	@Optional @Input Calendar startDate

	@Optional @Input Calendar endDate

	@Input Set<String> references = []
	
	@Input Set<String> skills = []

	@Inject
	AccomplishmentExtension(String name, ObjectFactory objectFactory) {
		this.name = name
	}
}
