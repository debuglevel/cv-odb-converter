package de.debuglevel.cvodb2xml

import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.export.xslt.XsltExporter
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import java.nio.file.Paths

class Main {
    fun start() {
        val odbPath = Paths.get("Lebenslauf.odb")
        val importer: Importer = OdbImporter(odbPath)
        val positions = importer.positions

        val xmlexporter = XmlExporter(Paths.get("Lebenslauf.xml"))
        xmlexporter.export(positions)

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