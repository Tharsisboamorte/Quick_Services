package com.tharsis.quickservices.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtil {

    fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }


    fun createTimeFromTimeStamp(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun formatTime(timestamp: Long): String {
        val formatter = SimpleDateFormat(Constants.TIME_FORMAT_DISPLAY, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }


    fun formatDateWithDay(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun combineDateAndTime(dateTimestamp: Long, timeTimestamp: Long): Long {
        val dateCalendar = Calendar.getInstance().apply {
            timeInMillis = dateTimestamp
        }
        val timeCalendar = Calendar.getInstance().apply {
            timeInMillis = timeTimestamp
        }

        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        dateCalendar.set(Calendar.SECOND, 0)
        dateCalendar.set(Calendar.MILLISECOND, 0)

        return dateCalendar.timeInMillis
    }

    fun isFutureDate(timestamp: Long): Boolean {
        return timestamp > System.currentTimeMillis()
    }

    fun getTodayAtMidnight(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getHourFromTimestamp(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.HOUR_OF_DAY)
    }


    fun getMinuteFromTimestamp(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.MINUTE)
    }
}