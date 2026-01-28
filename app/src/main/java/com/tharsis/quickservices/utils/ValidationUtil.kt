package com.tharsis.quickservices.utils

import android.util.Patterns
import com.tharsis.quickservices.domain.model.BookingStatus

object ValidationUtil {

    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Name is required"
            name.length < 3 -> "Name must be at least 3 characters"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }


    fun validatePhone(phone: String): String? {
        return when {
            phone.isBlank() -> "Phone number is required"
            else -> {
                val cleanPhone = phone.replace(Regex("[^0-9]"), "")
                if (cleanPhone.length in 10..11) null
                else "Invalid phone number format"
            }
        }
    }


    fun validateFutureDate(timestamp: Long): String? {
        return if (timestamp > System.currentTimeMillis()) {
            null
        } else {
            "Please select a future date and time"
        }
    }


    fun validateAmount(amount: Double): String? {
        return when {
            amount <= 0 -> "Invalid payment amount"
            else -> null
        }
    }


    fun validateBookingForPayment(status: BookingStatus): String? {
        return when (status) {
            BookingStatus.CANCELLED ->
                "Cannot pay for a cancelled booking"
            BookingStatus.COMPLETED ->
                "This booking has already been completed"
            BookingStatus.CONFIRMED ->
                "This booking has already been paid"
            else -> null
        }
    }
    
    fun validatePaymentAmount(amount: Double, bookingPrice: Double): String? {
        return if (amount == bookingPrice) {
            null
        } else {
            "Payment amount doesn't match booking price"
        }
    }
    
    fun sanitizeName(name: String): String {
        return name.trim()
    }
    
    fun sanitizeEmail(email: String): String {
        return email.trim().lowercase()
    }
    
    fun sanitizePhone(phone: String): String {
        return phone.trim()
    }
}