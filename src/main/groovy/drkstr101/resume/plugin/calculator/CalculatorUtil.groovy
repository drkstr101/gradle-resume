/**
 * 
 */
package drkstr101.resume.plugin.calculator

import drkstr101.resume.plugin.model.Skill

/**
 * @author Aaron R Miller
 *
 */
class CalculatorUtil {

	static SkillPoints mergeSkillPoints(Closure fn, SkillPoints... mapEntries) {
		return mapEntries*.keySet().sum().collectEntries { k ->
			[k, mapEntries.findAll{ it.containsKey k }*.get(k).inject(fn)]
		}
	}

	static SkillPoints addSkillPoints(SkillPoints... mapEntries) {
		return mergeSkillPoints({ a, b -> a + b }, mapEntries)
	}
	
	static Set<Skill> collectParents(Skill skill, Set<Skill> collector = []) {
		if(skill.parent) {
			collector << skill.parent
			return collectParents(skill.parent, collector)
		}
		
		return collector
	}
}
