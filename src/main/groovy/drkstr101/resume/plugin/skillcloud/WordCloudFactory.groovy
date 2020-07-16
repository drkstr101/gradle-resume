package drkstr101.resume.plugin.skillcloud;

import java.awt.Dimension

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.palette.ColorPalette

public class WordCloudFactory {

	static WordCloud create(final List<WordFrequency> wordFrequencies, final Dimension dimension, final ColorPalette colorPalette) {
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE)
		wordCloud.padding = 2
		wordCloud.background = new RectangleBackground(dimension)
		wordCloud.backgroundColor = EquidistantColorPalette.BG_COLOR
		wordCloud.colorPalette = colorPalette
		wordCloud.fontScalar = new LinearFontScalar(12, 72)
		wordCloud.build(wordFrequencies)
		
		return wordCloud
	}
}
