package com.tharsis.quickservices.domain.usecase.booking

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.repository.ServiceRepository
import jakarta.inject.Inject

class UpdateBookingUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(booking: Booking) {
        serviceRepository.updateBooking(booking)
    }
}