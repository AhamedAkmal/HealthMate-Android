package com.healthmate.app.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    private val displayFormatter = DateTimeFormatter.ofPattern(
        "EEEE, d MMMM yyyy",
        Locale.UK
    )

    fun todayDisplay(): String {
        return LocalDate.now().format(displayFormatter)
    }

    fun formatForDisplay(isoDate: String): String {
        return try {
            LocalDate.parse(isoDate).format(displayFormatter)
        } catch (_: Exception) {
            isoDate
        }
    }
}
