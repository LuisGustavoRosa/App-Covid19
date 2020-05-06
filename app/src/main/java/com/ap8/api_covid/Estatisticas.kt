package com.ap8.api_covid

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Estatisticas (
    var country: String,
    var cases: Int,
    var confirmed: Int,
    var deaths: Int,
    var recovered: Int,
    var date: String,
    var hour: String
) {
    override fun toString(): String {
        return date
    }

    fun getData(date: String): String {
        val diaString = date
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
    }
}