package com.tharsis.quickservices.domain.usecase.payment

import com.tharsis.quickservices.domain.repository.CieloLioPaymentRepository
import com.tharsis.quickservices.utils.AppResult
import javax.inject.Inject

class CancelPaymentUseCase @Inject constructor(
    private val cieloLioPaymentRepository: CieloLioPaymentRepository
) {
    suspend operator fun invoke(paymentId: String): AppResult<Unit> {
        return cieloLioPaymentRepository.cancelPayment(paymentId)
    }
}