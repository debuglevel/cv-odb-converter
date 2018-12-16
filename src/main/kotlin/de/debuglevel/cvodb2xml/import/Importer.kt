package de.debuglevel.cvodb2xml.import

import de.debuglevel.cvodb2xml.model.Position

interface Importer {
    val positions: List<Position>
}