/**
 * 
 */
package drkstr101.resume.plugin.skillcloud

import java.awt.Dimension

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.palette.ColorPalette

import drkstr101.resume.plugin.calculator.SkillPointCalculator
import drkstr101.resume.plugin.calculator.SkillPoints
import drkstr101.resume.plugin.model.Resume
import drkstr101.resume.plugin.model.Skill

/**
 * @author Aaron R Miller
 *
 */
class RenderSkillCloud extends DefaultTask {

	static String displayNameFor(Skill skill) {
		return skill.label? skill.label :
				skill.name.split('_').collect({ it.capitalize() }).join(" ")
	}
	
	static List<String> collectCategories(Map<String, Skill> skillsByName) {
		return skillsByName.values()
				.stream()
				.filter({ Skill skill -> skill.parent == null })
				.collect { Skill skill -> skill.name }
	}

	static List<WordFrequency> toWordFrequencies(Resume resume, Map<String, Integer> skillPoints, List<String> categories) {
		return skillPoints.collect([], { key, val ->
			def skill = resume.skillsByName.get(key)
			def index = categories.indexOf(skill.rootNode().name)
			return new WordFrequency(displayNameFor(skill), val, index)
		}).sort({ WordFrequency val -> val.frequency }).reverse()
	}


	static final DEFAULT_IMAGE_WIDTH = 800

	static final DEFAULT_IMAGE_HEIGHT = 600

	@Input final Property<Integer> imageWidth

	@Input final Property<Integer> imageHeight

	@Input final Provider<SkillPoints> skillPoints

	@OutputFile final RegularFileProperty imageOutputFile

	@OutputFile final RegularFileProperty textOutputFile

	@Internal Provider<Resume> modelProvider

	@Internal SkillPointCalculator _calculator


	@Inject
	RenderSkillCloud(ObjectFactory objectFactory, ProviderFactory providerFactory, ProjectLayout layout) {

		imageOutputFile = objectFactory.fileProperty()
		imageOutputFile.convention(layout.buildDirectory.file('skillcloud/skillcloud.png'))

		textOutputFile = objectFactory.fileProperty()
		textOutputFile.convention(layout.buildDirectory.file('skillcloud/skills.txt'))

		imageWidth = objectFactory.property(Integer)
		imageWidth.convention(DEFAULT_IMAGE_WIDTH)

		imageHeight = objectFactory.property(Integer)
		imageHeight.convention(DEFAULT_IMAGE_HEIGHT)

		skillPoints = providerFactory.provider {
			if(_calculator == null) {
				_calculator = new SkillPointCalculator(modelProvider.get()).calculate()
			}

			return _calculator.skillPoints
		}
	}

	@TaskAction
	void run() {
		final Resume resume = modelProvider.get()
		final Map<String, BigInteger> skillPoints = this.skillPoints.get()
		final List<String> categories = collectCategories(resume.skillsByName)
		final List<WordFrequency> wordFrequencies = toWordFrequencies(resume, skillPoints, categories)
		
		final Dimension dimension = new Dimension(imageWidth.get(), imageHeight.get())
		final ColorPalette colorPalette = new ColorPalette(EquidistantColorPalette.getColors(categories.size()))
		final WordCloud wordCloud = WordCloudFactory.create(wordFrequencies, dimension, colorPalette)

		textOutputFile.get().asFile.withWriter { Writer writer ->
			wordFrequencies.each { writer.write("${it.frequency}: ${it.word}\n") }
		}

		// Generate skill cloud image
		this.imageOutputFile.get().asFile.withOutputStream { os ->
			wordCloud.writeToStreamAsPNG(os)
		}
	}
}
