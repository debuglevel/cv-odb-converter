package de.debuglevel.cvodb2xml.writers

import de.debuglevel.cvodb2xml.export.json.JsonExporter
import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill
import java.nio.file.Path

object JsonWriter {
    fun write(
        positions: List<Position>,
        skills: List<Skill>,
        outputPath: Path,
    ) {
        writePositions(positions, outputPath)
        writeSkills(skills, outputPath)
    }

    private fun writeSkills(
        skills: List<Skill>,
        outputPath: Path
    ) {
        val jsonSkills = JsonExporter().export(skills)
        outputPath.resolve("skills.json").toFile().writeText(jsonSkills)
    }

    private fun writePositions(
        positions: List<Position>,
        outputPath: Path
    ) {
        val jsonPositions = JsonExporter().export(positions)
        outputPath.resolve("positions.json").toFile().writeText(jsonPositions)
    }
}
