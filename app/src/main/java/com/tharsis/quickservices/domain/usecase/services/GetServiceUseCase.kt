package com.tharsis.quickservices.domain.usecase.services

import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import kotlinx.coroutines.flow.Flow

class GetServiceUseCase constructor(
    private val serviceRepository: ServiceRepository
) {
    operator fun invoke(): Flow<AppResult<List<Service>>> {
        return serviceRepository.getAllServices()
    }
}