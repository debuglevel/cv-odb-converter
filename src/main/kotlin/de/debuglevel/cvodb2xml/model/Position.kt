package de.debuglevel.cvodb2xml.model

import java.time.LocalDate

data class Position(
    val id: Int,
    val title: String,
    val description: String,
    val begin: LocalDate,
    val end: LocalDate?,
    val place: String,
    val placeUrl: String,
    val category: String
)