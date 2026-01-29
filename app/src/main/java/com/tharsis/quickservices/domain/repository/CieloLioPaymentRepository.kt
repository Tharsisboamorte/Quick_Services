package com.tharsis.quickservices.domain.repository

import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.utils.AppResult

interface CieloLioPaymentRepository {
    suspend fun processPayment(payment: Payment, booking: Booking): AppResult<Payment>
    suspend fun cancelPayment(orderId: String): AppResult<Unit>
}