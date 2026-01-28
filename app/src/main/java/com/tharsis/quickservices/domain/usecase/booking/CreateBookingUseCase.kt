package com.tharsis.quickservices.domain.usecase.booking

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(booking: Booking): AppResult<Booking> {
        return serviceRepository.createBooking(booking)
    }

    suspend fun addToCalendar(booking: Booking): AppResult<String> {
        return serviceRepository.addToGoogleCalendar(booking)
    }
}