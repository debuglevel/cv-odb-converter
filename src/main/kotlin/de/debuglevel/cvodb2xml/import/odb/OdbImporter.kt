package de.debuglevel.cvodb2xml.import.odb

import de.debuglevel.cvodb2xml.UnzipUtility
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.import.hsqldb.HsqldbImporter
import de.debuglevel.cvodb2xml.model.Position
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

class OdbImporter(odbPath: Path) : Importer {
    private val logger = KotlinLogging.logger {}

    private val hsqldbImporter: HsqldbImporter

    init {
        logger.debug { "Initializing OdbImporter..." }

        logger.debug { "Creating temporary directory for ODB extraction..." }
        val odbExtractedPath = createTempDir()
        logger.debug { "Created temporary directory for ODB extraction: ${odbExtractedPath.absolutePath}" }

        logger.debug { "Unzipping ODB '${odbPath.toAbsolutePath()}' to '${odbExtractedPath.absolutePath}'..." }
        UnzipUtility().unzip(odbPath.toAbsolutePath().toString(), odbExtractedPath.absolutePath)
        logger.debug { "Unzipped ODB '${odbPath.toAbsolutePath()}' to '${odbExtractedPath.absolutePath}'" }

        val databasePath = odbExtractedPath.resolve("database")
        databasePath.listFiles().forEach {
            logger.debug { "Renaming ${it.toPath()} to ${it.toPath().resolveSibling("db.${it.name}")}..." }
            Files.move(it.toPath(), it.toPath().resolveSibling("database.${it.name}"))
        }

        hsqldbImporter = HsqldbImporter(databasePath)
    }

    override val positions: List<Position>
        get() = hsqldbImporter.positions
}