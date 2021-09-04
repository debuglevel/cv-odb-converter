package de.debuglevel.cvodb2xml.writers

import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill
import java.nio.file.Path

object HtmlWriter {
    fun write(
        positions: List<Position>,
        skills: List<Skill>,
        outputPath: Path,
        transformationPath: Path,
    ) {
        // produce HTML by serializing XML and converting it via XSL-T
        var html = transformationPath.resolve("replacement-template.html").toFile().readText()

        html = writePositions(positions, transformationPath, outputPath, html)
        html = writeSkills(skills, transformationPath, outputPath, html)

        outputPath.resolve("index.html").toFile().writeText(html)
    }

    private fun writeSkills(
        skills: List<Skill>,
        transformationPath: Path,
        outputPath: Path,
        html: String
    ): String {
        val xmlSkills = XmlExporter().export(skills)
        outputPath.resolve("temp-skills.xml").toFile().writeText(xmlSkills)

        val xsltSkillsResult = XsltExporter().export(xmlSkills, transformationPath.resolve("xml2html-skills.xsl"))
        outputPath.resolve("temp-skills.html").toFile().writeText(xsltSkillsResult)

        return html.replace("<!-- XSL-T skills placeholder -->", xsltSkillsResult)
    }

    private fun writePositions(
        positions: List<Position>,
        transformationPath: Path,
        outputPath: Path,
        html: String
    ): String {
        val xmlPositions = XmlExporter().export(positions)
        outputPath.resolve("temp-positions.xml").toFile().writeText(xmlPositions)

        val xsltPositionsResult =
            XsltExporter().export(xmlPositions, transformationPath.resolve("xml2html-positions.xsl"))
        outputPath.resolve("temp-positions.html").toFile().writeText(xsltPositionsResult)

        return html.replace("<!-- XSL-T positions placeholder -->", xsltPositionsResult)
    }
}