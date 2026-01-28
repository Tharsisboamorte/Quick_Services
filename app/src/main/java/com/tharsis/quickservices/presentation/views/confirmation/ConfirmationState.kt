package com.tharsis.quickservices.presentation.views.confirmation

import com.tharsis.quickservices.domain.model.Booking

data class ConfirmationState(
    val booking: Booking? = null,
    val isLoading: Boolean = false,
    val isAddingToCalendar: Boolean = false,
    val calendarEventId: String? = null,
    val errorMessage: String? = null
)
