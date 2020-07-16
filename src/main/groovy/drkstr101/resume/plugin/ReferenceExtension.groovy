package drkstr101.resume.plugin

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * @author Aaron R Miller
 *
 */
class ReferenceExtension implements Named {

	private final String name

	@Input
	String getName() {
		return name
	}

	@Optional @Input String description

	@Inject
	ReferenceExtension(String name, ObjectFactory objectFactory) {
		this.name = name
	}
}
