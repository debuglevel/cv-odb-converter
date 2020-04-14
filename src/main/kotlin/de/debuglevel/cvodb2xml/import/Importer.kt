package de.debuglevel.cvodb2xml.import

import de.debuglevel.cvodb2xml.model.Position
import de.debuglevel.cvodb2xml.model.Skill

interface Importer {
    val positions: List<Position>
    val skills: List<Skill>
}