package de.debuglevel.cvodb2xml

import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.odb.OdbImporter
import java.nio.file.Paths

class Main {
    fun start() {
        val odbPath = Paths.get("Lebenslauf.odb")
        val importer: Importer = OdbImporter(odbPath)
        val positions = importer.positions
        val exporter = XmlExporter(Paths.get("Lebenslauf.xml"))
        exporter.export(positions)
    }
}

fun main(args: Array<String>) {
    Main().start()
}