package com.tharsis.quickservices.domain.usecase.services

import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import javax.inject.Inject


class GetServiceByIdUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(serviceId: String) : AppResult<Service> {
        return serviceRepository.getServiceById(serviceId)
    }
}