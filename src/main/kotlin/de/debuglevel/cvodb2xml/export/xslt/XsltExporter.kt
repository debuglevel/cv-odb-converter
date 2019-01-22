package de.debuglevel.cvodb2xml.export.xslt

import mu.KotlinLogging
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Path
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class XsltExporter {
    private val logger = KotlinLogging.logger {}

    fun export(xml: String, xslt: Path): String {
        logger.debug { "Transforming XML via XSL-T..." }

        return try {
            val stylesheetSource = StreamSource(xslt.toFile())
            val transformer = TransformerFactory.newInstance()
                .newTransformer(stylesheetSource)

            val xmlStringreader = StringReader(xml)
            val xmlSource = StreamSource(xmlStringreader)
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