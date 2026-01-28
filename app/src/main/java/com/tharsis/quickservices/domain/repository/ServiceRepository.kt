package com.tharsis.quickservices.domain.repository

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.utils.AppResult
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {

    fun getAllServices(): Flow<AppResult<List<Service>>>

    suspend fun getServiceById(serviceId: String): AppResult<Service>

    suspend fun createBooking(booking: Booking): AppResult<Booking>

    suspend fun getBookingById(bookingId: String): AppResult<Booking>

    suspend fun updateBooking(booking: Booking): AppResult<Booking>

    fun getUserBookings(email: String): Flow<AppResult<List<Booking>>>

    suspend fun processPayment(payment: Payment): AppResult<Payment>

    suspend fun getPaymentById(paymentId: String): AppResult<Payment>

    suspend fun addToGoogleCalendar(booking: Booking): AppResult<String>
}