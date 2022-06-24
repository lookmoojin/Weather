package com.piglet.weather.domain

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

interface DateTimeInterface {
    fun getCurrentTimeMillis(): Long
    fun getCurrentDateTime(dateFormat: String): String
    fun convertTimeMillisToDate(milli: Long, dateFormat: String): String
}

object DateTimeUtil : DateTimeInterface {

    override fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    @SuppressLint("SimpleDateFormat")
    override fun getCurrentDateTime(dateFormat: String): String {
        val sdf2 = SimpleDateFormat(dateFormat)
        sdf2.timeZone = TimeZone.getDefault()
        val calendar = Calendar.getInstance(TimeZone.getDefault())

        val current = calendar.timeInMillis
        return sdf2.format(current)
    }

    override fun convertTimeMillisToDate(milli: Long, dateFormat: String): String {
        val simpleDateFormat: DateFormat
        val date = Date(milli)
        val calendar = GregorianCalendar()
        calendar.time = date
        simpleDateFormat = SimpleDateFormat(
            dateFormat,
            Locale("en", "th")
        )
        return simpleDateFormat.format(calendar.time)
    }
}
