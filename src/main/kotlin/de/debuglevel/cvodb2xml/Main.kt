package de.debuglevel.cvodb2xml

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import de.debuglevel.cvodb2xml.model.Position
import mu.KotlinLogging
import java.nio.file.Paths
import java.time.LocalDate

class Main : CliktCommand() {
    private val logger = KotlinLogging.logger {}

    private val odbPath by argument("odb", help = "Path to ODB database file").path(true, true, false, false, true)
    private val xsltPath by option("--xslt", help = "Path to XSL-T stylesheet file").path(
        true,
        true,
        false,
        false,
        true
    ).default(Paths.get("xml2html.xsl"))
    private val templatePath by option("--template", help = "Path to template file").path(
        true,
        true,
        false,
        false,
        true
    ).default(Paths.get("replacement-template.html"))
    private val outputPath by option("--output", help = "Path to output file").path(
        false,
        true,
        false,
        true,
        false
    ).default(Paths.get("index.html"))

    override fun run() {
        val importer: Importer = OdbImporter(odbPath)

        val positions = importer.positions.sortedWith(
            compareBy<Position>
            {
                when (it.end) {
                    null -> LocalDate.now()
                    else -> it.end
                }
            }
                .thenBy { it.begin }
                .reversed())

        val xml = XmlExporter().export(positions)
        //File("Lebenslauf.xml").writeText(xml)

        val xsltResult = XsltExporter().export(xml, xsltPath)
        //File("Lebenslauf.html").writeText(xsltResult)

        val html = templatePath.toFile().readText().replace("<!-- XSLT input -->", xsltResult)
        outputPath.toFile().writeText(html)
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}