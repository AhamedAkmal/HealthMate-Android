package com.healthmate.app.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object DatePickerUtils {

    fun localDateToMillis(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun millisToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}