package com.tharsis.quickservices.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtil {

    private val displayDateFormatter = SimpleDateFormat(
        Constants.DATE_FORMAT_DISPLAY,
        Locale.getDefault()
    )

    private val displayTimeFormatter = SimpleDateFormat(
        Constants.TIME_FORMAT_DISPLAY,
        Locale.getDefault()
    )

    private val fullDateTimeFormatter = SimpleDateFormat(
        Constants.DATETIME_FORMAT_FULL,
        Locale.getDefault()
    )

    private val isoDateTimeFormatter = SimpleDateFormat(
        Constants.DATETIME_FORMAT_ISO,
        Locale.getDefault()
    )

    /**
    * Formats a timestamp to display date format (dd/MM/yyyy).
    */
    fun formatDate(timestamp: Long): String {
        return displayDateFormatter.format(Date(timestamp))
    }

    /**
     * Formats a timestamp to display time format (HH:mm).
     */
    fun formatTime(timestamp: Long): String {
        return displayTimeFormatter.format(Date(timestamp))
    }

    /**
     * Formats a timestamp to ISO 8601 format for API/Database storage.
     */
    fun formatIsoDateTime(timestamp: Long): String {
        return isoDateTimeFormatter.format(Date(timestamp))
    }

    /**
     * Creates a Calendar instance from timestamp.
     */
    fun getCalendarFromTimestamp(timestamp: Long): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
    }

    /**
     * Combines date and time into a single timestamp.
     */
    fun combineDateAndTime(dateTimestamp: Long, timeTimestamp: Long): Long {
        val dateCalendar = getCalendarFromTimestamp(dateTimestamp)
        val timeCalendar = getCalendarFromTimestamp(timeTimestamp)

        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        dateCalendar.set(Calendar.SECOND, 0)
        dateCalendar.set(Calendar.MILLISECOND, 0)

        return dateCalendar.timeInMillis
    }

    /**
    * Checks if a timestamp is in the future.
    */
    fun isFutureDate(timestamp: Long): Boolean {
        return timestamp > System.currentTimeMillis()
    }

    /**
     * Checks if a timestamp is today.
     */
    fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val date = getCalendarFromTimestamp(timestamp)

        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * Gets the minimum selectable date (current time).
     */
    fun getMinimumSelectableDate(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Adds duration in minutes to a timestamp.
     */
    fun addMinutes(timestamp: Long, minutes: Int): Long {
        val calendar = getCalendarFromTimestamp(timestamp)
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.timeInMillis
    }

    /**
     * Calculates the end time based on start time and duration.
     */
    fun calculateEndTime(startTimestamp: Long, durationMinutes: Int): Long {
        return addMinutes(startTimestamp, durationMinutes)
    }

    /**
     * Creates a Calendar event description from booking details.
     */
    fun createCalendarEventDescription(
        serviceName: String,
        price: Double,
        location: String?
    ): String {
        val builder = StringBuilder()
        builder.append("Service: $serviceName\n")
        builder.append("Price: R$ %.2f".format(price))
        location?.let {
            builder.append("\nLocation: $it")
        }
        return builder.toString()
    }

}