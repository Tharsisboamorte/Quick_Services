package com.tharsis.quickservices.domain.model

data class Payment(
    val id: String,
    val bookingId: String,
    val amount: Double,
    val status: PaymentStatus,
    val paymentMethod: PaymentMethod,
    val transactionId: String? = null,
    val cieloOrderId: String? = null,
    val errorMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
) {

    fun getFormattedAmount(): String = "R$ %.2f".format(amount)

    fun isSuccessful(): Boolean = status == PaymentStatus.COMPLETED

    fun isFailed(): Boolean = status == PaymentStatus.FAILED

    fun isProcessing(): Boolean = status == PaymentStatus.PROCESSING
}

enum class PaymentStatus(val displayName: String) {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    PROCESSING("Processing")
}

enum class PaymentMethod(val displayName: String) {
    CREDIT("Credit Card"),
    DEBIT("Debit Card"),
    PIX("PIX")
}