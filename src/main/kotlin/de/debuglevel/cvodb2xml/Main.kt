package de.debuglevel.cvodb2xml

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.graph.SkillNodeInformationRetriever
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill
import de.debuglevel.graphlibrary.GraphBuilder
import de.debuglevel.graphlibrary.GraphUtils
import de.debuglevel.graphlibrary.export.DotExporter
import de.debuglevel.graphlibrary.export.GraphvizExporter
import guru.nidi.graphviz.engine.Format
import mu.KotlinLogging
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate

class Main : CliktCommand() {
    private val logger = KotlinLogging.logger {}

    private val odbPath by argument("odb", help = "Path to ODB database file").path(
        exists = true,
        fileOkay = true,
        folderOkay = false,
        writable = false,
        readable = true
    )

    private val positionsXsltPath by option(
        "--positions-xslt",
        help = "Path to XSL-T stylesheet file for positions"
    ).path(
        exists = true,
        fileOkay = true,
        folderOkay = false,
        writable = false,
        readable = true
    ).default(Paths.get("xml2html-positions.xsl"))

    private val skillsXsltPath by option("--skills-xslt", help = "Path to XSL-T stylesheet file for skills").path(
        exists = true,
        fileOkay = true,
        folderOkay = false,
        writable = false,
        readable = true
    ).default(Paths.get("xml2html-skills.xsl"))

    private val templatePath by option("--template", help = "Path to template file").path(
        exists = true,
        fileOkay = true,
        folderOkay = false,
        writable = false,
        readable = true
    ).default(Paths.get("replacement-template.html"))

    private val outputPath by option("--output", help = "Path to output file").path(
        exists = false,
        fileOkay = true,
        folderOkay = false,
        writable = true,
        readable = false
    ).default(Paths.get("index.html"))

    override fun run() {
        var html = templatePath.toFile().readText()

        val importer: Importer = OdbImporter(odbPath)

        val positions = importer.positions
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

        val skills = importer.skills
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

        // experimental visualization of skill comparisons
        val graph = GraphBuilder<Skill>(SkillNodeInformationRetriever(skillComparisons)).build(skills, false)
        val dot = DotExporter.generate(graph)
        GraphvizExporter.render(dot, File("skills.svg"), Format.SVG)
        GraphUtils.findCycles(graph) // TODO: this should stop the whole tool if a cycle was found

        GraphUtils.populateOrders(graph)
        // set orders according to scaled orders in graph
        graph.getVertices().forEach { vertex ->
            skills.first { skill -> skill == vertex.content }.order = vertex.scaledOrder
        }
        graph.getVertices().sortedBy { it.order }.forEach { println("${it.scaledOrder} ${it.text}") }

        val orderedSkills = skills
            .sortedWith(
                compareBy<Skill>
                {
                    it.category
                }
                    .thenBy { it.subcategory }
                    .thenByDescending { it.order }
                    .thenBy { it.label }
            )

        // produce HTML by serializing XML and converting it via XSL-T
        val xmlPositions = XmlExporter().export(positions)
        File("temp-positions.xml").writeText(xmlPositions)
        val xsltPositionsResult = XsltExporter().export(xmlPositions, positionsXsltPath)
        File("temp-positions.html").writeText(xsltPositionsResult)
        html = html.replace("<!-- XSL-T positions placeholder -->", xsltPositionsResult)

        val xmlSkills = XmlExporter().export(orderedSkills)
        File("temp-skills.xml").writeText(xmlSkills)
        val xsltSkillsResult = XsltExporter().export(xmlSkills, skillsXsltPath)
        File("temp-skills.html").writeText(xsltSkillsResult)
        html = html.replace("<!-- XSL-T skills placeholder -->", xsltSkillsResult)

        outputPath.toFile().writeText(html)
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}