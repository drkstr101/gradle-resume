/**
 * 
 */
package drkstr101.resume.plugin.markdown

import org.gradle.workers.WorkAction

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.data.MutableDataSet

/**
 * @author Aaron R Miller
 *
 */
abstract class RenderMarkdownWork implements WorkAction<RenderMarkdownParamaters> {

	private static MutableDataSet _options

	public static MutableDataSet getOptions() {
		if(_options == null) {
			_options = new MutableDataSet()
			_options.set(Parser.EXTENSIONS,
					Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()))
		}

		return _options;
	}

	private static Parser _parser

	public static Parser getParser() {
		if(_parser == null) {
			_parser = Parser.builder(getOptions()).build()
		}

		return _parser;
	}

	private static HtmlRenderer _renderer

	public static HtmlRenderer getRenderer() {
		if(_renderer == null) {
			_renderer = HtmlRenderer.builder(getOptions()).build()
		}

		return _renderer;
	}


	@Override
	void execute() {
		final RenderMarkdownParamaters paramaters = this.parameters
		final String content = paramaters.content.get()
		final File outputFile = paramaters.outputFile.get().asFile
		final Document document = getParser().parse(content)

		outputFile.withWriter { Writer writer ->
			getRenderer().render(document, writer)
		}
	}
}
