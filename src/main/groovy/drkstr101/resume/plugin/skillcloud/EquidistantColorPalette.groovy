/**
 * 
 */
package drkstr101.resume.plugin.skillcloud

import java.awt.Color

/**
 * @author Aaron R Miller
 *
 */
class EquidistantColorPalette {

	static final Color BG_COLOR = new Color(0xFFFFFF)

	static final COLORS = [
		new Color(0x003f5c),
		new Color(0x2f4b7c),
		new Color(0x665191),
		new Color(0xa05195),
		new Color(0xd45087),
		new Color(0xf95d6a),
		new Color(0xff7c43),
		new Color(0xffa600)
	]

	static Color[] getColors(int length = 8) {
		if(length > 8) throw new RuntimeException("Cannot select more than 8 colors")
		if(length == 1) return COLORS.take(1)

		def range = (0..7).step((8 / length) as int)
		return COLORS[range]
	}
}
