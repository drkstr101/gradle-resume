package drkstr101.resume.plugin.calculator

import java.math.BigInteger

/**
 * @author Aaron R Miller
 *
 */
class SkillPoints extends LinkedHashMap<String, BigInteger> {
	static final int SCALE = 12
	
	static BigInteger one() {
		return SCALE
	}
	
	static BigInteger half() {
		return SCALE / 2
	}
	
	static BigInteger third() {
		return SCALE / 3
	}
	
	static BigInteger fourt() {
		return SCALE / 4
	}
}
