package com.luc.common

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun Date.toFormat(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun dateDiffer(date: String): String {
    val oldDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).parse(date)
    val diff = (Calendar.getInstance().time.time - (oldDate?.time ?: 0))

    val seconds = (diff / 1000) +1
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val week = days / 7

    var dateRest = "$seconds s"
    if (seconds > 60) dateRest = "$minutes m"
    if (minutes > 60) dateRest = "$hours h"
    if (hours > 24) dateRest = "$days d"
    if (days > 7) dateRest = "$week d"

    return dateRest
}
