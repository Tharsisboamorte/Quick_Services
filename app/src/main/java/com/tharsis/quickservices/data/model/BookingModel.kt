package com.tharsis.quickservices.data.model

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.BookingStatus

data class BookingModel(
    @DocumentId
    val id: String = "",
    @PropertyName("service_id")
    val serviceId: String = "",
    @PropertyName("service_name")
    val serviceName: String = "",
    @PropertyName("service_price")
    val servicePrice: Double = 0.0,
    @PropertyName("service_duration_minutes")
    val serviceDurationMinutes: Int = 0,
    @PropertyName("scheduled_timestamp")
    val scheduledTimestamp: Long = 0L,
    @PropertyName("customer_name")
    val customerName: String = "",
    @PropertyName("customer_email")
    val customerEmail: String = "",
    @PropertyName("customer_phone")
    val customerPhone: String = "",
    @PropertyName("status")
    val status: String = "",
    @PropertyName("notes")
    val notes: String? = null,
    @PropertyName("created_at")
    val createdAt: Long = 0L,
    @PropertyName("calendar_event_id")
    val calendarEventId: String? = null
) {
    fun toDomain(): Booking {
        return Booking(
            id = id,
            serviceId = serviceId,
            serviceName = serviceName,
            servicePrice = servicePrice,
            serviceDurationMinutes = serviceDurationMinutes,
            scheduledTimestamp = scheduledTimestamp,
            customerName = customerName,
            customerEmail = customerEmail,
            customerPhone = customerPhone,
            status = parseStatusFromString(status),
            notes = notes,
            createdAt = createdAt,
            calendarEventId = calendarEventId
        )
    }

    private fun parseStatusFromString(statusString: String): BookingStatus {
        return try {
            BookingStatus.valueOf(statusString.uppercase())
        } catch (e: Exception) {
            Log.e(TAG, "parse Status Error: ${e.message}", e)
            BookingStatus.PENDING
        }
    }

    companion object {
        const val TAG = "BookingModel"

        fun fromDomain(booking: Booking): BookingModel {
            return BookingModel(
                id = booking.id,
                serviceId = booking.serviceId,
                serviceName = booking.serviceName,
                servicePrice = booking.servicePrice,
                serviceDurationMinutes = booking.serviceDurationMinutes,
                scheduledTimestamp = booking.scheduledTimestamp,
                customerName = booking.customerName,
                customerEmail = booking.customerEmail,
                customerPhone = booking.customerPhone,
                status = booking.status.name,
                notes = booking.notes,
                createdAt = booking.createdAt,
                calendarEventId = booking.calendarEventId
            )
        }
    }
}
