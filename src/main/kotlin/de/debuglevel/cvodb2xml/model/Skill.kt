package de.debuglevel.cvodb2xml.model

import java.awt.Color

data class Skill(
    val id: Int,
    val category: String,
    val level: String,
    val label: String,
    val description: String?,
    val subcategory: String?,
    var order: Double? = null
) {
    override fun toString(): String {
        return label
    }

    val hexColor: String?
        get() {
            return if (order != null) {
                val x = getTrafficlightColor(order!!)
                val color = Color(x)
                val hex = java.lang.String.format("#%02x%02x%02x", color.red, color.green, color.blue)
                hex
            } else {
                null
            }
        }

    fun getTrafficlightColor(value: Double): Int {
        // 0 = red -> 26°/0.07 = red
        // 1 = green -> 162°/0.45 = green
        val saturation = 0.3f
        val brightness = 0.86f
        //val hue = value.toFloat() / 3f

        val oldMin = 0
        val oldMax = 1
        val newMax = 0.45
        val newMin = 0.07
        val oldValue = value

        val oldRange = (oldMax - oldMin)
        val newRange = (newMax - newMin)
        val newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin

        val hue = newValue.toFloat()

        return Color.HSBtoRGB(hue, saturation, brightness)
    }
}