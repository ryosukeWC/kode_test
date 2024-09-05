package com.example.kode.presentation.feature.details.common

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class FormatDateAndPhone {

    fun formatDate(localDate: LocalDate) : String {

        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("ru"))

        return "${localDate.dayOfMonth} $month ${localDate.year}"
    }

    fun formatPhone(phone: String) : String {

        val phoneWithDigitsOnly = phone.filter { it.isDigit() }

        if (phoneWithDigitsOnly.length != 10) {
            throw IllegalArgumentException("Incorrect format of phone")
        }

        return "+7 (${phoneWithDigitsOnly.substring(0, 3)}) ${phoneWithDigitsOnly.substring(3, 6)} ${phoneWithDigitsOnly.substring(6, 8)} ${phoneWithDigitsOnly.substring(8, 10)}"
    }
}