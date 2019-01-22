package de.debuglevel.cvodb2xml.export.xml

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import mu.KotlinLogging

class XmlExporter {
    private val logger = KotlinLogging.logger {}

    fun export(obj: Any): String {
        logger.debug { "Exporting to XML string..." }

        val xmlMapper = XmlMapper()
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)

        return xmlMapper.writeValueAsString(obj)
    }
}