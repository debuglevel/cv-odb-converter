package de.debuglevel.cvodb2xml.import.odb

import com.github.rjeschke.txtmark.Processor
import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.model.Position
import mu.KotlinLogging
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDate

class OdbImporter(private val odbPath: Path) : Importer {
    private val logger = KotlinLogging.logger {}

    init {
        logger.debug { "Initializing OdbImporter..." }

        logger.debug { "Loading OdbDriver..." }
        Class.forName("de.debuglevel.odbjdbc.OdbDriver")
    }

    override val positions: List<Position>
        get() {
            val databasePath = odbPath.toFile().invariantSeparatorsPath

            var connection: Connection? = null
            val database = "jdbc:odb://file=$databasePath"

            val positions = mutableListOf<Position>()

            try {
                // Create database connection
                logger.debug { "Opening connection to $database..." }
                connection = DriverManager.getConnection(database)

                // Create and execute statement
                val statement = connection.createStatement()
                val resultset = statement.executeQuery("SELECT * FROM \"PUBLIC\".\"Positionen\"")

                while (resultset.next()) {
                    logger.debug { "Reading next position from database..." }
                    positions.add(
                        Position(
                            resultset.getString("id").toInt(),
                            resultset.getString("positiontitle"),
                            Processor.process(resultset.getString("description")),
                            LocalDate.parse(resultset.getString("begindate")),
                            if (resultset.getString("enddate").isNullOrBlank()) null else LocalDate.parse(
                                resultset.getString(
                                    "enddate"
                                )
                            ),
                            resultset.getString("place"),
                            resultset.getString("placeurl"),
                            resultset.getString("category"),
                            resultset.getString("industrialsector"),
                            resultset.getString("placesize"),
                            resultset.getString("department"),
                            resultset.getString("activity")
                        )
                    )
                }

                resultset.close()
                statement.close()
            } catch (e: SQLException) {
                logger.error(e) { }
                throw e
            } finally {
                try {
                    connection?.close()
                } catch (e: SQLException) {
                    logger.error(e) { }
                    throw e
                }
            }

            return positions
        }
}