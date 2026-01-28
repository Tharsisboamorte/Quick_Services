package com.tharsis.quickservices.domain.usecase.booking

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserBookingsUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    operator fun invoke( email: String ): Flow<AppResult<List<Booking>>>{
       return serviceRepository.getUserBookings(email)
    }
}