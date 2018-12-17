package de.debuglevel.cvodb2xml.import.hsqldb

import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.model.Position
import mu.KotlinLogging
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDate

class HsqldbImporter(private val hsqldbPath: Path) : Importer {
    private val logger = KotlinLogging.logger {}

    override val positions: List<Position>
        get() {
            val databasePath = hsqldbPath.toFile().invariantSeparatorsPath

            var connection: Connection? = null
            val database = "jdbc:hsqldb:file:$databasePath/database"
            val user = "SA"
            val password = ""

            val positions = mutableListOf<Position>()

            try {
                // Create database connection
                logger.debug { "Opening connection to $database..." }
                connection = DriverManager.getConnection(database, user, password)

                // Create and execute statement
                val statement = connection.createStatement()
                val resultset = statement.executeQuery("SELECT * FROM \"PUBLIC\".\"Positionen\"")

                while (resultset.next()) {
                    logger.debug { "Reading next position from database..." }
                    positions.add(
                        Position(
                            resultset.getString("id").toInt(),
                            resultset.getString("positiontitle"),
                            resultset.getString("description"),
                            LocalDate.parse(resultset.getString("begindate")),
                            if (resultset.getString("enddate").isNullOrBlank()) null else LocalDate.parse(
                                resultset.getString(
                                    "enddate"
                                )
                            ),
                            resultset.getString("place"),
                            resultset.getString("placeurl"),
                            resultset.getString("category")
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