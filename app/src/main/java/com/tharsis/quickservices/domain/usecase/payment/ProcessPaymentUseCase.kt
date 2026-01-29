package com.tharsis.quickservices.domain.usecase.payment

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.repository.CieloLioPaymentRepository
import com.tharsis.quickservices.utils.AppResult
import javax.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
   private val cieloLioPaymentRepository: CieloLioPaymentRepository
) {
    suspend operator fun invoke(
        payment: Payment,
        booking: Booking
    ): AppResult<Payment> {
        return  cieloLioPaymentRepository.processPayment(payment = payment, booking = booking)
    }
}