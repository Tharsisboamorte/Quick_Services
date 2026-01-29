package com.tharsis.quickservices.presentation.views.payment

import com.tharsis.quickservices.domain.model.PaymentMethod
import com.tharsis.quickservices.utils.InstallmentOption

data class PaymentState(
    val bookingId: String = "",
    val selectedInstallment: InstallmentOption? = null,
    val installmentOptions: List<InstallmentOption> = emptyList(),
    val isLoading: Boolean = false,
    val amount: Double = 0.0,
    val serviceName: String = "",
    val customerName: String = "",
    val customerEmail: String = "",
    val serviceDuration: Int = 0,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CREDIT,
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val paymentId: String? = null,
    val transactionId: String? = null,
    val errorMessage: String? = null
)