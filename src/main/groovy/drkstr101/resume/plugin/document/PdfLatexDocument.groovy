/**
 * 
 */
package drkstr101.resume.plugin.document

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations

/**
 * @author Aaron R Miller
 *
 */
class PdfLatexDocument extends DefaultTask {
	
	@Internal final ExecOperations execOperations
	
	@Internal final FileSystemOperations fileSystemOperations
	
	@Input final Property<String> fileName
	
	@InputDirectory final DirectoryProperty inputDirectory
	
	@OutputDirectory final DirectoryProperty outputDirectory
	
	@Inject
	PdfLatexDocument(ObjectFactory objectFactory, ProjectLayout layout, ExecOperations execOperations, FileSystemOperations fileSystemOperations) {
		
		this.execOperations = execOperations
		this.fileSystemOperations = fileSystemOperations
		
		fileName = objectFactory.property(String)
		fileName.convention("main.tex")
		
		inputDirectory = objectFactory.directoryProperty()
		inputDirectory.convention(layout.projectDirectory.dir("template/default"))
		
		outputDirectory = objectFactory.directoryProperty()
		outputDirectory.convention(layout.buildDirectory.dir("document/default"))
	}

	@TaskAction
	protected void run() {
		
		// Copy template files into the output directory
		fileSystemOperations.copy {
			from inputDirectory
			into outputDirectory
		}
		
		// Run pdf latex
		final String fileName = this.fileName.get()
		final File outputDir = this.outputDirectory.get().asFile
		
		execOperations.exec {
			executable = 'pdflatex'
			workingDir = outputDir
			args = [fileName]
			
			// squash non-error output
			standardOutput = new ByteArrayOutputStream()
		}
	}
}
