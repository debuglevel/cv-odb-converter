package de.debuglevel.cvodb2xml.writers

import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill
import java.io.File
import java.nio.file.Path

object HtmlWriter {
    fun write(
        positions: List<Position>,
        skills: List<Skill>,
        htmlOutputPath: Path,
        templatePath: Path,
        positionsXsltPath: Path,
        skillsXsltPath: Path,
    ) {
        // produce HTML by serializing XML and converting it via XSL-T
        var html = templatePath.toFile().readText()

        html = writePositions(positions, positionsXsltPath, html)
        html = writeSkills(skills, skillsXsltPath, html)

        htmlOutputPath.toFile().writeText(html)
    }

    private fun writeSkills(
        skills: List<Skill>,
        skillsXsltPath: Path,
        html: String
    ): String {
        var html1 = html
        val xmlSkills = XmlExporter().export(skills)
        File("data/output/temp-skills.xml").writeText(xmlSkills)
        val xsltSkillsResult = XsltExporter().export(xmlSkills, skillsXsltPath)
        File("data/output/temp-skills.html").writeText(xsltSkillsResult)
        html1 = html1.replace("<!-- XSL-T skills placeholder -->", xsltSkillsResult)
        return html1
    }

    private fun writePositions(
        positions: List<Position>,
        positionsXsltPath: Path,
        html: String
    ): String {
        var html1 = html
        val xmlPositions = XmlExporter().export(positions)
        File("data/output/temp-positions.xml").writeText(xmlPositions)
        val xsltPositionsResult = XsltExporter().export(xmlPositions, positionsXsltPath)
        File("data/output/temp-positions.html").writeText(xsltPositionsResult)
        html1 = html1.replace("<!-- XSL-T positions placeholder -->", xsltPositionsResult)
        return html1
    }
}