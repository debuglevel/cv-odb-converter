package de.debuglevel.cvodb2xml

import de.debuglevel.cvodb2xml.export.xml.XmlFileExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import mu.KotlinLogging
import java.nio.file.Paths

class Main {
    private val logger = KotlinLogging.logger {}

    fun start() {
        val odbPath = Paths.get("Lebenslauf.odb")
        val importer: Importer = OdbImporter(odbPath)
        val positions = importer.positions

        val xmlFileExporter = XmlFileExporter(Paths.get("Lebenslauf.xml"))
        xmlFileExporter.export(positions)

        val xsltExporter = XsltExporter(
            Paths.get("xml2html.xsl"),
            Paths.get("Lebenslauf.html")
        )
        xsltExporter.export(positions)
    }
}

fun main(args: Array<String>) {
    Main().start()
}