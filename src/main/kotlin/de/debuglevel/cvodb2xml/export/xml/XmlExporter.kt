package de.debuglevel.cvodb2xml.export.xml

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import de.debuglevel.cvodb2xml.export.Exporter
import mu.KotlinLogging

class XmlExporter : Exporter {
    private val logger = KotlinLogging.logger {}

    lateinit var xml: String

    override fun export(obj: Any) {
        logger.debug { "Exporting to XML string..." }

        val xmlMapper = XmlMapper()
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)

        this.xml = xmlMapper.writeValueAsString(obj)
    }
}