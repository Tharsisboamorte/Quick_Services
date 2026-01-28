package com.tharsis.quickservices.presentation.views.payment

import com.tharsis.quickservices.domain.model.PaymentMethod
import com.tharsis.quickservices.utils.InstallmentOption

data class PaymentState(
    val bookingId: String = "",
    val amount: Double = 0.0,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CREDIT,
    val selectedInstallment: InstallmentOption? = null,
    val installmentOptions: List<InstallmentOption> = emptyList(),
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val paymentId: String? = null
)