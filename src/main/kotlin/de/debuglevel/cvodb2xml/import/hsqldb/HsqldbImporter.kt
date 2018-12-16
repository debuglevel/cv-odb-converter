package de.debuglevel.cvodb2xml.import.hsqldb

import de.debuglevel.cvodb2xml.import.Importer
import de.debuglevel.cvodb2xml.model.Position
import mu.KotlinLogging
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDate

class HsqldbImporter(val hsqldbPath: File) : Importer {
    private val logger = KotlinLogging.logger {}


    init {
    }

    override val positions: List<Position>
        get() {
            val path = hsqldbPath.invariantSeparatorsPath

            var conn: Connection? = null
            val db = "jdbc:hsqldb:file:$path/database"
            val user = "SA"
            val password = ""

            val positions = mutableListOf<Position>()

            try {
                // Create database connection
                logger.debug { "Opening connection to $db..." }
                conn = DriverManager.getConnection(db, user, password)

                // Create and execute statement
                val stmt = conn.createStatement()
                val rs =
                    stmt.executeQuery("SELECT * FROM \"PUBLIC\".\"Positionen\"")

                // Loop through the data and print all artist names
                while (rs.next()) {
                    positions.add(
                        Position(
                            rs.getString("id").toInt(),
                            rs.getString("positiontitle"),
                            rs.getString("description"),
                            LocalDate.parse(rs.getString("begindate")),
                            LocalDate.parse(rs.getString("enddate")),
                            rs.getString("place"),
                            rs.getString("placeurl"),
                            rs.getString("category")
                        )
                    )
                }

                // Clean up
                rs.close()
                stmt.close()
            } catch (e: SQLException) {
                logger.error(e) { }
            } finally {
                try {
                    // Close connection
                    conn?.close()
                } catch (e: SQLException) {
                    logger.error(e) { }
                }
            }

            return positions
        }
}