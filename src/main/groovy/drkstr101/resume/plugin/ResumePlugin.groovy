/**
 *
 */
package drkstr101.resume.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

import drkstr101.resume.plugin.document.PdfLatexDocument
import drkstr101.resume.plugin.markdown.RenderMarkdown
import drkstr101.resume.plugin.skillcloud.RenderSkillCloud

/**
 * @author Aaron R Miller
 *
 */
public class ResumePlugin implements Plugin<Project> {

	public void apply(Project project) {

		// Ensure life-cycle tasks are available
		project.pluginManager.apply(BasePlugin)
		final ResumeExtension resume = project.extensions.create('resume', ResumeExtension)
		
		// Register task to render markdown content
		final RenderMarkdown markdown = project.tasks.create('markdown', RenderMarkdown)
		markdown.group = "Build"
		markdown.resume = resume

		// Register task to generate Skill Cloud image
		final RenderSkillCloud skillCloud = project.tasks.create('skillCloud', RenderSkillCloud)
		skillCloud.group = "Build"
		skillCloud.modelProvider = new ResumeModelProvider(resume)
		
		// Register task to build the latext document
		final PdfLatexDocument document = project.tasks.create('document', PdfLatexDocument)
		document.group = "Build"

		final Task build = project.tasks.getByName('build')
		build.dependsOn(markdown, skillCloud, document)
	}
}
