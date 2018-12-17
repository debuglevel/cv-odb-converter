package de.debuglevel.cvodb2xml.export.xml

import de.debuglevel.cvodb2xml.export.Exporter
import mu.KotlinLogging
import java.nio.file.Path

class XmlFileExporter(private val xmlFile: Path) : Exporter {
    private val logger = KotlinLogging.logger {}

    override fun export(obj: Any) {
        logger.debug { "Exporting to XML file '$xmlFile'..." }

        val xmlExporter = XmlExporter()
        xmlExporter.export(obj)

        logger.debug { "Writing to XML file '$xmlFile'..." }
        xmlFile.toFile().writeText(xmlExporter.xml)
    }
}