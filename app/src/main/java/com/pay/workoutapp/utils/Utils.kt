package com.pay.workoutapp.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    const val DATE_FORMAT: String = "dd-MMM-yyyy"

    //This is an extension function
    //usage:
    //val timestampt = Date()
    //val dateString = timestamp.dateToString("dd-MMM-yyyy")
    fun Date.dateToString(format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }
}