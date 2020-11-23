package de.debuglevel.cvodb2xml.graph

import de.debuglevel.cvodb2xml.model.Skill
import de.debuglevel.cvodb2xml.model.SkillComparison
import de.debuglevel.graphlibrary.Color
import de.debuglevel.graphlibrary.NodeInformationRetriever
import de.debuglevel.graphlibrary.Shape
import de.debuglevel.graphlibrary.Vertex
import mu.KotlinLogging

class SkillNodeInformationRetriever(
    private val skillComparisons: List<SkillComparison>
) : NodeInformationRetriever<Skill> {
    private val logger = KotlinLogging.logger {}

    override fun getText(node: Skill): String {
        return node.label
    }

    override fun getColor(node: Skill): Color {
        return Color.Gray
    }

    override fun getShape(node: Skill): Shape {
        return Shape.Ellipse
    }

    override fun getTooltip(node: Skill): String {
        return ""
    }

    override fun getPrecedingVertices(
        vertex: Vertex<Skill>,
        allVertices: List<Vertex<Skill>>
    ): List<Vertex<Skill>> {
        val currentBetterSkill = vertex.content

        val worseSkills = skillComparisons
            .filter { it.betterSkill == currentBetterSkill }
            .map { it.worseSkill }

        val worseSkillVertices = allVertices.filter { worseSkills.contains(it.content) }

        return worseSkillVertices
    }

    override fun getSucceedingVertices(
        vertex: Vertex<Skill>,
        allVertices: List<Vertex<Skill>>
    ): List<Vertex<Skill>> {
        val currentWorseSkill = vertex.content

        val betterSkills = skillComparisons
            .filter { it.worseSkill == currentWorseSkill }
            .map { it.betterSkill }

        val betterSkillVertices = allVertices.filter { betterSkills.contains(it.content) }

        return betterSkillVertices
    }
}