package de.debuglevel.cvodb2xml.export.xml

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import de.debuglevel.cvodb2xml.export.Exporter
import java.nio.file.Path

class XmlExporter(val xmlFile: Path) : Exporter {
    override fun export(obj: Any) {
        val xmlMapper = XmlMapper()
        // use the line of code for pretty-print XML on console. We should remove it in production.
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)

        xmlMapper.writeValue(xmlFile.toFile(), obj)
    }
}