package de.debuglevel.cvodb2xml

import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import de.debuglevel.cvodb2xml.model.Position
import mu.KotlinLogging
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate

class Main {
    private val logger = KotlinLogging.logger {}

    fun start() {
        val odbPath = Paths.get("Lebenslauf.odb")
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
        File("Lebenslauf.xml").writeText(xml)

        val xsltResult = XsltExporter().export(xml, Paths.get("xml2html.xsl"))
        File("Lebenslauf.html").writeText(xsltResult)
    }
}

fun main(args: Array<String>) {
    Main().start()
}