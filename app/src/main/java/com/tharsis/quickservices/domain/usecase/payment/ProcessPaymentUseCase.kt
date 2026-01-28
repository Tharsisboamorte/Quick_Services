package com.tharsis.quickservices.domain.usecase.payment

import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.AppResult
import jakarta.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
   private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(
        payment: Payment
    ): AppResult<Payment> {
        return  serviceRepository.processPayment(payment)
    }
}