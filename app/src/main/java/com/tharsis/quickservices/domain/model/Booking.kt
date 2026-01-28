package com.tharsis.quickservices.domain.model

data class Booking(
    val id: String,
    val serviceId: String,
    val serviceName: String,
    val servicePrice: Double,
    val serviceDurationMinutes: Int,
    val scheduledTimestamp: Long,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val status: BookingStatus,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val calendarEventId: String? = null
) {

    // Gets the end timestamp based on start time and duration.
    fun getEndTimestamp(): Long {
        return scheduledTimestamp + (serviceDurationMinutes * 60 * 1000)
    }

    // Checks if the booking can be cancelled.
    fun canBeCancelled(): Boolean {
        return status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED
    }

    fun isPast(): Boolean {
        return scheduledTimestamp < System.currentTimeMillis()
    }

    // Checks if the booking is within the next 24 hours.
    fun isUpcoming(): Boolean {
        val now = System.currentTimeMillis()
        val twentyFourHoursFromNow = now + (24 * 60 * 60 * 1000)
        return scheduledTimestamp in now..twentyFourHoursFromNow
    }

}


enum class BookingStatus(val displayName: String) {
    PENDING("Pending Payment"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed")
}