package com.tharsis.quickservices.data.repository

import android.content.Context
import com.google.api.services.calendar.Calendar
import com.tharsis.quickservices.data.network.FirebaseDataSource
import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import com.tharsis.quickservices.utils.map
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
        TODO("Not yet implemented")
    }

    override suspend fun createBooking(booking: Booking): AppResult<Booking> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookingById(bookingId: String): AppResult<Booking> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBooking(booking: Booking): AppResult<Booking> {
        TODO("Not yet implemented")
    }

    override fun getUserBookings(): Flow<AppResult<List<Booking>>> {
        TODO("Not yet implemented")
    }

    override suspend fun processPayment(payment: Payment): AppResult<Payment> {
        TODO("Not yet implemented")
    }

    override suspend fun getPaymentById(paymentId: String): AppResult<Payment> {
        TODO("Not yet implemented")
    }

    override suspend fun addToGoogleCalendar(booking: Booking): AppResult<String> {
        TODO("Not yet implemented")
    }

}