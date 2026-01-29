package com.tharsis.quickservices.presentation.views.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.BookingStatus
import com.tharsis.quickservices.domain.usecase.booking.CreateBookingUseCase
import com.tharsis.quickservices.domain.usecase.services.GetServiceByIdUseCase
import com.tharsis.quickservices.domain.usecase.services.GetServiceUseCase
import com.tharsis.quickservices.utils.AppResult
import com.tharsis.quickservices.utils.Constants
import com.tharsis.quickservices.utils.DateTimeUtil
import com.tharsis.quickservices.utils.ValidationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getServicesUseCase: GetServiceUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val getServiceByIdUseCase: GetServiceByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(BookingState())
    val state: StateFlow<BookingState> = _state.asStateFlow()

    private val serviceId: String = checkNotNull(savedStateHandle[Constants.ARG_SERVICE_ID]) {
        "serviceId is required"
    }

    init {
        loadService()
    }

    private fun loadService() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getSelectedService(serviceId)
        }
    }

    fun updateCustomerName(name: String) {
        _state.update { it.copy(customerName = name) }
    }

    fun updateCustomerEmail(email: String) {
        _state.update { it.copy(customerEmail = email) }
    }

    fun updateCustomerPhone(phone: String) {
        _state.update { it.copy(customerPhone = phone) }
    }

    fun updateNotes(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    fun selectDate(timestamp: Long) {
        _state.update { it.copy(selectedDate = timestamp) }
    }

    fun selectTime(timestamp: Long) {
        _state.update { it.copy(selectedTime = timestamp) }
    }

    fun createBooking() {
        val currentState = _state.value
        if (!currentState.isFormValid() || currentState.service == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val scheduledTimestamp = DateTimeUtil.combineDateAndTime(
                dateTimestamp = currentState.selectedDate!!,
                timeTimestamp = currentState.selectedTime!!
            )

            val futureDateError = ValidationUtil.validateFutureDate(scheduledTimestamp)
            if (futureDateError != null) {
                _state.update { it.copy(isLoading = false, errorMessage = futureDateError) }
                return@launch
            }

            val notes = currentState.notes.trim().takeIf { it.isNotBlank() }
            val service = currentState.service

            val booking = Booking(
                id = Booking.generateID(),
                serviceId = service.id,
                serviceName = service.name,
                servicePrice = service.price,
                serviceDurationMinutes = service.durationMinutes,
                scheduledTimestamp = scheduledTimestamp,
                customerName = ValidationUtil.sanitizeName(currentState.customerName),
                customerEmail = ValidationUtil.sanitizeEmail(currentState.customerEmail),
                customerPhone = ValidationUtil.sanitizePhone(currentState.customerPhone),
                notes = notes,
                status = BookingStatus.PENDING,
                createdAt = System.currentTimeMillis(),
                calendarEventId = null
            )

            when (val result = createBookingUseCase(booking)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            bookingId = result.data.id,
                            isSuccess = true
                        )
                    }
                }
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun getSelectedService(serviceID: String){
        viewModelScope.launch {
            when (val result = getServiceByIdUseCase(serviceID)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            service = result.data,
                            isLoading = false
                        )
                    }
                }
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }

        }

    }
}