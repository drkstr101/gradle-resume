package drkstr101.resume.plugin

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import drkstr101.resume.plugin.model.EmploymentType

/**
 * @author Aaron R Miller
 *
 */
class EmploymentExtension implements Named {

	private final String name

	@Input
	String getName() {
		return name
	}

	@Input String type
	@Optional @Input String title
	@Optional @Input Calendar startDate
	@Optional @Input Calendar endDate
	@Optional @Input String employer
	@Optional @Input String description
	@Optional @Input Set<String> accomplishments

	@Inject
	EmploymentExtension(String name, ObjectFactory objectFactory) {
		this.name = name
	}
}
