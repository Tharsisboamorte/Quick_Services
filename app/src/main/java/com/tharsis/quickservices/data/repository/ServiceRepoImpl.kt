package com.tharsis.quickservices.data.repository

import android.content.Context
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.EventReminder
import com.tharsis.quickservices.data.model.BookingModel
import com.tharsis.quickservices.data.model.PaymentModel
import com.tharsis.quickservices.data.network.FirebaseDataSource
import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.PaymentStatus
import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import com.tharsis.quickservices.utils.Constants
import com.tharsis.quickservices.utils.DateTimeUtil
import com.tharsis.quickservices.utils.map
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date

@Singleton
class ServiceRepoImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val firebaseDataSrc: FirebaseDataSource,
    private val calendarService: Calendar? = null
) : ServiceRepository {
    override fun getAllServices(): Flow<AppResult<List<Service>>> {
        return firebaseDataSrc.getAllServices().map { result ->
            result.map { serviceModels ->
                serviceModels.map { it.toDomain() }
            }
        }
    }

    override suspend fun getServiceById(serviceId: String): AppResult<Service> {
        return firebaseDataSrc.getServiceById(serviceId).map { it.toDomain() }
    }

    override suspend fun createBooking(booking: Booking): AppResult<Booking> {
        val bookingModel = BookingModel.fromDomain(booking)
        return firebaseDataSrc.createBooking(bookingModel).map { it.toDomain() }
    }

    override suspend fun getBookingById(bookingId: String): AppResult<Booking> {
        return firebaseDataSrc.getBookingById(bookingId).map { it.toDomain() }
    }

    override suspend fun updateBooking(booking: Booking): AppResult<Booking> {
        val bookingModel = BookingModel.fromDomain(booking)
        return firebaseDataSrc.updateBooking(bookingModel).map { it.toDomain() }
    }

    override fun getUserBookings(email: String): Flow<AppResult<List<Booking>>> {
        val userEmail = email.lowercase()
        return firebaseDataSrc.getUserBookings(userEmail).map { result ->
            result.map { bookingModels ->
                bookingModels.map { it.toDomain() }
            }
        }
    }

    override suspend fun processPayment(payment: Payment): AppResult<Payment> = withContext(Dispatchers.IO) {
        try {

            val paymentModel = PaymentModel.fromDomain(payment)
            val result = firebaseDataSrc.createPayment(paymentModel)

            if (result is AppResult.Error) {
                return@withContext result.map { it.toDomain() }
            }

            val processedPayment = processCieloLioPayment(payment)

            val updatedModel = PaymentModel.fromDomain(processedPayment)
            firebaseDataSrc.updatePayment(updatedModel)

            AppResult.Success(processedPayment)
        } catch (e: Exception) {
            AppResult.Error<Exception>(e.message.toString())
        } as AppResult<Payment>
    }

    override suspend fun getPaymentById(paymentId: String): AppResult<Payment> {
        return firebaseDataSrc.getPaymentById(paymentId).map { it.toDomain() }
    }

    override suspend fun addToGoogleCalendar(booking: Booking): AppResult<String> = withContext(Dispatchers.IO){
        if (calendarService == null) {
            return@withContext AppResult.Error("Calendar service not initialized")
        }

        try {
            val event = Event().apply {
                summary = booking.serviceName
                description = DateTimeUtil.createCalendarEventDescription(
                    booking.serviceName,
                    booking.servicePrice,
                    null
                )

                val startDateTime = EventDateTime()
                    .setDateTime(DateTime(Date(booking.scheduledTimestamp)))
                start = startDateTime

                val endDateTime = EventDateTime()
                    .setDateTime(DateTime(Date(booking.getEndTimestamp())))
                end = endDateTime

                reminders = Event.Reminders().apply {
                    useDefault = false
                    overrides = listOf(
                        EventReminder().apply {
                            method = "popup"
                            minutes = Constants.CALENDAR_EVENT_REMINDER_MINUTES
                        }
                    )
                }
            }

            val createdEvent = calendarService.events()
                .insert("primary", event)
                .execute()

            AppResult.Success(createdEvent.id)
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

    private suspend fun processCieloLioPayment(payment: Payment): Payment {
        kotlinx.coroutines.delay(2000)

        val isSuccess = (0..100).random() > 10

        return if (isSuccess) {
            payment.copy(
                status = PaymentStatus.COMPLETED,
                transactionId = "TXN${System.currentTimeMillis()}",
                cieloOrderId = "ORDER${System.currentTimeMillis()}",
                completedAt = System.currentTimeMillis()
            )
        } else {
            payment.copy(
                status = PaymentStatus.FAILED,
                errorMessage = Constants.ERROR_PAYMENT_FAILED
            )
        }
    }

}