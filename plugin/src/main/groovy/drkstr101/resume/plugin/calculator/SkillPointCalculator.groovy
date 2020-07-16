/**
 * 
 */
package drkstr101.resume.plugin.calculator

import static drkstr101.resume.plugin.calculator.CalculatorUtil.*


import drkstr101.resume.plugin.model.Accomplishment
import drkstr101.resume.plugin.model.Resume
import drkstr101.resume.plugin.model.Skill

/**
 * @author Aaron R Miller
 *
 */
class SkillPointCalculator {
	
	private final SkillPoints _skillPoints = [:]

	public SkillPoints getSkillPoints() {
		return _skillPoints
	}

	private int _totalSkillPoints


	public int getTotalSkillPoints() {
		return _totalSkillPoints
	}

	private final Resume _resume

	/**
	 * @param model
	 */
	SkillPointCalculator(Resume resume) {
		_resume = resume
	}


	/**
	 * Calculates all relevant skill point totals and 
	 * caches the result. This may be accomplished 
	 * through multiple passes through the Resume model.
	 * 
	 * @return
	 */
	SkillPointCalculator calculate() {
		// println "Calculating skill points..."

		//initialize each skill with a base value of 1 point
		SkillPoints baseSkillPoints = _resume.skillsByName.collectEntries { key, val ->
			[key, SkillPoints.one()]
		}

		_totalSkillPoints = baseSkillPoints.values().sum()
		
		// println "---- BASE SKILL POINTS ----"
		// println baseSkillPoints

		_resume.accomplishments.each { Accomplishment accomplishment ->
			SkillPoints addedSkillPoints = accomplishment.skills.inject([:]) { Map memo, Skill skill ->
				// add one point every time a skill is referenced in an accomplishment
				memo.put(skill.name, SkillPoints.one())

				// add a half  point to each of its parents
				memo.putAll addSkillPoints(memo as SkillPoints, collectParents(skill).collectEntries({ 
					[it.name, SkillPoints.half()] 
				}) as SkillPoints)
				
				return memo
			}

			if(!addedSkillPoints.isEmpty()) {
				 // println("---- APPENDING SKILL POINTS ----")
				 // println addedSkillPoints
				
				_totalSkillPoints += addedSkillPoints.values().sum()

				baseSkillPoints.putAll(addSkillPoints(baseSkillPoints, addedSkillPoints))
			}
		}

		 // println "---- ENDING SKILL POINTS ----"
		 // println baseSkillPoints

		_skillPoints.putAll(baseSkillPoints)

		return this
	}

}
