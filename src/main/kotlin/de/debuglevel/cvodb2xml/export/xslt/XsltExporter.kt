package de.debuglevel.cvodb2xml.export.xslt

import de.debuglevel.cvodb2xml.export.Exporter
import de.debuglevel.cvodb2xml.export.xml.XmlExporter
import mu.KotlinLogging
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Path
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class XsltExporter(
    private val xsltFile: Path,
    private val outFile: Path
) : Exporter {
    private val logger = KotlinLogging.logger {}

    override fun export(obj: Any) {
        logger.debug { "Retrieving XML source..." }
        val xmlExporter = XmlExporter()
        xmlExporter.export(obj)

        val xmlRaw = xmlExporter.xml
        val xml = if (!xmlRaw.isEmpty()) {
            xmlRaw
        } else {
            throw Exception("XML must not be empty")
        }

        val xsltResult = transform(xml)

        logger.debug { "Writing output to '$outFile'..." }
        outFile.toFile().writeText(xsltResult)
    }

    private fun transform(xml: String): String {
        logger.debug { "Transforming XML via XSL-T..." }

        return try {
            val xmlStringreader = StringReader(xml)

            val xsltStylesheet = xsltFile.toFile()
            val stylesheetSource = StreamSource(xsltStylesheet)
            val xmlSource = StreamSource(xmlStringreader)
            val transformer = TransformerFactory.newInstance()
                .newTransformer(stylesheetSource)

            val xsltResultStringwriter = StringWriter()
            transformer.transform(xmlSource, StreamResult(xsltResultStringwriter))

            val xsltResult = xsltResultStringwriter.toString()
            xsltResultStringwriter.close()

            xsltResult
        } catch (e: TransformerException) {
            logger.error(e) { }
            throw e
        }
    }
}