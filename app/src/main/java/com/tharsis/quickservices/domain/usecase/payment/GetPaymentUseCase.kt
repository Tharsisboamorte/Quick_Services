package com.tharsis.quickservices.domain.usecase.payment

import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(paymentId: String): AppResult<Payment> {
        return serviceRepository.getPaymentById(paymentId)
    }
}