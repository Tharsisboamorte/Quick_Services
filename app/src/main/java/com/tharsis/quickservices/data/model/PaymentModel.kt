package com.tharsis.quickservices.data.model

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.PaymentMethod
import com.tharsis.quickservices.domain.model.PaymentStatus

data class PaymentModel(
    @DocumentId
    val id: String = "",
    @PropertyName("booking_id")
    val bookingId: String = "",
    @PropertyName("amount")
    val amount: Double = 0.0,
    @PropertyName("payment_method")
    val paymentMethod: String = "",
    @PropertyName("status")
    val status: String = "",
    @PropertyName("transaction_id")
    val transactionId: String? = null,
    @PropertyName("cielo_order_id")
    val cieloOrderId: String? = null,
    @PropertyName("error_message")
    val errorMessage: String? = null,
    @PropertyName("created_at")
    val createdAt: Long = 0L,
    @PropertyName("completed_at")
    val completedAt: Long? = null
) {

    fun toDomain(): Payment {
        return Payment(
            id = id,
            bookingId = bookingId,
            amount = amount,
            paymentMethod = parsePaymentMethodFromString(paymentMethod),
            status = parsePaymentStatusFromString(status),
            transactionId = transactionId,
            cieloOrderId = cieloOrderId,
            errorMessage = errorMessage,
            createdAt = createdAt,
            completedAt = completedAt
        )
    }

    private fun parsePaymentMethodFromString(methodString: String): PaymentMethod {
        return try {
            PaymentMethod.valueOf(methodString.uppercase())
        } catch (e: Exception) {
            Log.e(TAG, "parse payment Method Error: ${e.message}", e)
            PaymentMethod.CREDIT
        }
    }

    private fun parsePaymentStatusFromString(statusString: String): PaymentStatus {
        return try {
            PaymentStatus.valueOf(statusString.uppercase())
        } catch (e: Exception) {
            Log.e(TAG, "parse payment Status Error: ${e.message}", e)
            PaymentStatus.PENDING
        }
    }

    companion object {
       const val TAG = "PaymentModel"

        fun fromDomain(payment: Payment): PaymentModel {
            return PaymentModel(
                id = payment.id,
                bookingId = payment.bookingId,
                amount = payment.amount,
                paymentMethod = payment.paymentMethod.name,
                status = payment.status.name,
                transactionId = payment.transactionId,
                cieloOrderId = payment.cieloOrderId,
                errorMessage = payment.errorMessage,
                createdAt = payment.createdAt,
                completedAt = payment.completedAt
            )
        }
    }
}