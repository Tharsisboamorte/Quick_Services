package com.tharsis.quickservices.domain.usecase.booking

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import jakarta.inject.Inject

class GetBookingUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(bookingId: String): AppResult<Booking> {
        return serviceRepository.getBookingById(bookingId)
    }
}