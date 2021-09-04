package de.debuglevel.cvodb2xml.writers

import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill
import mu.KotlinLogging
import java.nio.file.Path

object XmlWriter {
    private val logger = KotlinLogging.logger {}

    fun write(
        positions: List<Position>,
        skills: List<Skill>,
        outputPath: Path,
    ) {
        logger.debug { "Writing..." }

        writePositions(positions, outputPath)
        writeSkills(skills, outputPath)
    }

    private fun writeSkills(
        skills: List<Skill>,
        outputPath: Path
    ) {
        val xmlSkills = XmlExporter().export(skills)
        outputPath.resolve("skills.xml").toFile().writeText(xmlSkills)
    }

    private fun writePositions(
        positions: List<Position>,
        outputPath: Path
    ) {
        val xmlPositions = XmlExporter().export(positions)
        outputPath.resolve("positions.xml").toFile().writeText(xmlPositions)
    }
}