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
    val xsltFile: Path,
    val outFile: Path
) : Exporter {

    private val logger = KotlinLogging.logger {}


    override fun export(obj: Any) {
        val xmlExporter = XmlExporter()
        xmlExporter.export(obj)
        val xml = xmlExporter.xml

        val result = try {

            val stringReader = StringReader(xml)

            val stylesheet = xsltFile.toFile()
            //val xmlfile = File("D://employees.xml")
            val stylesource = StreamSource(stylesheet)
            val xmlsource = StreamSource(stringReader)
            val transformer = TransformerFactory.newInstance()
                .newTransformer(stylesource)


            val stringwriter = StringWriter()

            // Transform the document and store it in a file
            transformer.transform(xmlsource, StreamResult(stringwriter))

            //val consoleOut = StreamResult(System.out)
            // Transform the document and print the content to console
            //transformer.transform(xmlsource, consoleOut)

            val s = stringwriter.toString()
            stringwriter.close()

            s
        } catch (e: TransformerException) {
            logger.error(e) { }
            throw e
        }

        outFile.toFile().writeText(result)

        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}