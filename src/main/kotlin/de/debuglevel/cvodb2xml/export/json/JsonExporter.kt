package de.debuglevel.cvodb2xml.export.json

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import mu.KotlinLogging

class JsonExporter {
    private val logger = KotlinLogging.logger {}

    fun export(obj: Any): String {
        logger.debug { "Exporting to JSON string..." }

        val jsonMapper = JsonMapper()
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT)

        return jsonMapper.writeValueAsString(obj)
    }
}