package com.tharsis.quickservices.presentation.views.booking

import com.tharsis.quickservices.domain.model.Service

data class BookingState(
    val service: Service? = null,
    val selectedDate: Long? = null,
    val selectedTime: Long? = null,
    val customerName: String = "",
    val customerEmail: String = "",
    val customerPhone: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val bookingId: String? = null,
    val isSuccess: Boolean = false
) {
    fun isFormValid(): Boolean {
        return customerName.isNotBlank() &&
                customerEmail.isNotBlank() &&
                customerPhone.isNotBlank() &&
                selectedDate != null &&
                selectedTime != null
    }
}