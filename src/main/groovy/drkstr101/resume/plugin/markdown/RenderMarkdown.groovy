/**
 * 
 */
package drkstr101.resume.plugin.markdown

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor

import drkstr101.resume.plugin.AccomplishmentExtension
import drkstr101.resume.plugin.EmploymentExtension
import drkstr101.resume.plugin.ReferenceExtension
import drkstr101.resume.plugin.ResumeExtension

/**
 * Render all markdown content as html, and assembles it
 * in the output directory.
 * 
 * @author Aaron R Miller
 */
class RenderMarkdown extends DefaultTask {

	@Internal final WorkerExecutor workerExecutor

	@Internal final ProjectLayout layout

	@Nested final Property<ResumeExtension> resume

	@OutputDirectory final DirectoryProperty outputDirectory

	@Inject
	RenderMarkdown(ObjectFactory objectFactory, ProjectLayout layout, WorkerExecutor workerExecutor) {
		this.layout = layout

		this.outputDirectory = objectFactory.directoryProperty()
		this.outputDirectory.convention(layout.buildDirectory.dir("markdown"))

		resume = objectFactory.property(ResumeExtension)

		this.workerExecutor = workerExecutor
	}

	/**
	 *
	 */
	@TaskAction
	void run() {
		final ResumeExtension resume = this.resume.get()

		// Create a WorkQueue to submit work items
		final WorkQueue workQueue = workerExecutor.noIsolation()
		
		outputDirectory.get().dir("references").asFile.mkdirs()
		outputDirectory.get().dir("employers").asFile.mkdirs()
		outputDirectory.get().dir("accomplishments").asFile.mkdirs()

		// Render reference descriptions
		resume.references.each { ReferenceExtension reference ->
			if(reference.description) {
				workQueue.submit(RenderMarkdownWork) { RenderMarkdownParamaters parameters ->
					parameters.outputFile.set(outputDirectory.get().file("references/${reference.name}.htm"))
					parameters.content.set(reference.description)
				}
			}
		}

		// Render employment descriptions
		resume.employers.each { EmploymentExtension employment ->
			if(employment.description) {
				workQueue.submit(RenderMarkdownWork) { RenderMarkdownParamaters parameters ->
					parameters.outputFile.set(outputDirectory.get().file("employers/${employment.name}.htm"))
					parameters.content.set(employment.description)
				}
			}
		}

		// Render accomplishment descriptions
		resume.accomplishments.each { AccomplishmentExtension accomplishment ->
			if(accomplishment.description) {
				workQueue.submit(RenderMarkdownWork) { RenderMarkdownParamaters parameters ->
					parameters.outputFile.set(outputDirectory.get().file("accomplishments/${accomplishment.name}.htm"))
					parameters.content.set(accomplishment.description)
				}
			}
		}

		// Wait for all asynchronous work to complete
		workQueue.await()
	}
}
