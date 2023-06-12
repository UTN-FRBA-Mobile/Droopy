package com.example.droopy.utils

import androidx.compose.ui.text.capitalize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    private val dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

    fun toReadableString(dateTime: LocalDateTime): String {
        return dateTime.format(dateFormatter)
    }

    private val statusTranslations = mapOf("CREATED" to "En búsqueda")
    fun translateSearchInfoStatus(status: String): String {
        return statusTranslations[status] ?: "Unknown translation for $status"
    }

    fun translateBool(givenBool: Boolean, firstUpper: Boolean = false): String {
        val result = if (givenBool) "si" else "no"

        return if (firstUpper) result.replaceFirstChar(Char::titlecase) else result
    }
}