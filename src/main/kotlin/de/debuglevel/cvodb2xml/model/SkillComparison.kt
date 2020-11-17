package de.debuglevel.cvodb2xml.model

data class SkillComparison(
    val betterSkillId: Int,
    val worseSkillId: Int,
    val betterSkill: Skill,
    val worseSkill: Skill
) {
    override fun toString(): String {
        return "${betterSkill.label} > ${worseSkill.label}"
    }
}