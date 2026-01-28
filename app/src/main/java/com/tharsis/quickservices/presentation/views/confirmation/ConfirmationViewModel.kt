package com.tharsis.quickservices.presentation.views.confirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.domain.usecase.booking.CreateBookingUseCase
import com.tharsis.quickservices.utils.AppResult
import com.tharsis.quickservices.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val createBookingUseCase: CreateBookingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingId: String = checkNotNull(savedStateHandle[Constants.ARG_BOOKING_ID])

    private val _state = MutableStateFlow(ConfirmationState())
    val state: StateFlow<ConfirmationState> = _state.asStateFlow()

    init {
        loadBooking()
    }

    private fun loadBooking() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = serviceRepository.getBookingById(bookingId)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(booking = result.data, isLoading = false)
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

    fun addToCalendar() {
        val booking = _state.value.booking ?: return

        viewModelScope.launch {
            _state.update { it.copy(isAddingToCalendar = true) }

            when (val result = createBookingUseCase.addToCalendar(booking)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            isAddingToCalendar = false,
                            calendarEventId = result.data
                        )
                    }
                }
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isAddingToCalendar = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }
}