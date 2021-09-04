package de.debuglevel.cvodb2xml

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import de.debuglevel.cvodb2xml.graph.SkillNodeInformationRetriever
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill
import de.debuglevel.cvodb2xml.writers.HtmlWriter
import de.debuglevel.cvodb2xml.writers.JsonWriter
import de.debuglevel.cvodb2xml.writers.XmlWriter
import de.debuglevel.graphlibrary.GraphBuilder
import de.debuglevel.graphlibrary.GraphUtils
import de.debuglevel.graphlibrary.export.DotExporter
import de.debuglevel.graphlibrary.export.GraphvizExporter
import guru.nidi.graphviz.engine.Format
import mu.KotlinLogging
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate

class Application : CliktCommand() {
    private val logger = KotlinLogging.logger {}

    private val odbPath by argument("odb", help = "Path to ODB database file").path(
        exists = true,
        fileOkay = true,
        folderOkay = false,
        writable = false,
        readable = true
    )

    private val transformationPath by option(
        "--transformation-directory",
        help = "Path to transformation directory"
    ).path(
        exists = true,
        fileOkay = false,
        folderOkay = true,
        writable = true,
        readable = false
    ).default(Paths.get("data/transformation/"))

    private val outputPath by option("--output-directory", help = "Path to output directory").path(
        exists = true,
        fileOkay = false,
        folderOkay = true,
        writable = true,
        readable = false
    ).default(Paths.get("data/output/"))

    private val calculateSkillOrders by option(
        "--calculate-skill-orders",
        help = "Calculate skill orders defined by skill comparisons"
    ).flag(default = false)

    override fun run() {
        val importer: Importer = OdbImporter(odbPath)

        val positions = getPositions(importer)
        val skills = getSkills(importer, calculateSkillOrders)

        generate(positions, skills)
    }

    private fun generate(
        positions: List<Position>,
        skills: List<Skill>
    ) {
        JsonWriter.write(positions, skills, outputPath)
        XmlWriter.write(positions, skills, outputPath)
        HtmlWriter.write(positions, skills, outputPath, transformationPath)
    }

    private fun getSkills(
        importer: Importer,
        calculateSkillOrders: Boolean
    ): List<Skill> {
        val skillsRaw = importer.skills
            .sortedWith(
                compareBy<Skill>
                {
                    it.category
                }
                    .thenBy { it.subcategory }
                    .thenBy {
                        when (it.level) {
                            "high" -> 1
                            "medium" -> 2
                            "low" -> 3
                            else -> 0
                        }
                    }
                    .thenBy { it.label }
            )

        val skillComparisons = importer.skillComparisons

        val skills = if (calculateSkillOrders) {
            // experimental visualization of skill comparisons

            val graph = GraphBuilder<Skill>(SkillNodeInformationRetriever(skillComparisons)).build(skillsRaw, false)
            val dot = DotExporter.generate(graph)
            GraphvizExporter.render(dot, File("data/output/skills.svg"), Format.SVG)
            GraphUtils.findCycles(graph) // TODO: this should stop the whole tool if a cycle was found

            GraphUtils.populateOrders(graph)
            // set orders according to scaled orders in graph
            graph.getVertices().forEach { vertex ->
                skillsRaw.first { skill -> skill == vertex.content }.order = vertex.scaledOrder
            }
            graph.getVertices().sortedBy { it.order }.forEach { println("${it.scaledOrder} ${it.text}") }

            val orderedSkills = skillsRaw
                .sortedWith(
                    compareBy<Skill>
                    {
                        it.category
                    }
                        .thenBy { it.subcategory }
                        .thenByDescending { it.order }
                        .thenBy { it.label }
                )
            orderedSkills
        } else {
            skillsRaw
            // TODO: handle this case in XSL-T
        }
        return skills
    }

    private fun getPositions(importer: Importer): List<Position> {
        return importer.positions
            .sortedWith(
                compareBy<Position>
                {
                    when (it.end) {
                        null -> LocalDate.now()
                        else -> it.end
                    }
                }
                    .thenBy { it.begin }
                    .reversed())
    }
}

fun main(args: Array<String>) {
    Application().main(args)
}