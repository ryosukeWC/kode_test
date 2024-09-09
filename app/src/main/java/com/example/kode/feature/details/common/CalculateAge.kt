package com.example.kode.feature.details.common

import java.time.LocalDate
import java.time.Period

class CalculateAge {

    fun calculate(dateBirthday : LocalDate) : String {

        val currentDate = LocalDate.now()

        return Period.between(dateBirthday,currentDate).years.toString()

    }
}