package de.debuglevel.cvodb2xml.model

data class Skill(
    val id: Int,
    val category: String,
    val level: String,
    val label: String,
    val description: String?,
    val subcategory: String?
) {
    override fun toString(): String {
        return label
    }
}