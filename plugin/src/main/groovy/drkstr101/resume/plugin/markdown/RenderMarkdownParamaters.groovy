/**
 * 
 */
package drkstr101.resume.plugin.markdown

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters

/**
 * @author Aaron R Miller
 *
 */
interface RenderMarkdownParamaters extends WorkParameters {
	Property<String> getContent()
	RegularFileProperty getOutputFile()
}
