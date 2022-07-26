package com.example.cuidedevoce.Help

import java.text.SimpleDateFormat
import java.util.*

object dataConverter {

    fun dataConverter(dateString: String): Date? {
        val formatter = SimpleDateFormat("MM/dd/yyyy")

        val date = formatter.parse(dateString)

        return date
    }

}