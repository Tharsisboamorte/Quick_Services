package com.tharsis.quickservices.presentation.views.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.PaymentMethod
import com.tharsis.quickservices.domain.model.PaymentStatus
import com.tharsis.quickservices.domain.usecase.booking.GetUserBookingsUseCase
import com.tharsis.quickservices.domain.usecase.payment.CancelPaymentUseCase
import com.tharsis.quickservices.domain.usecase.payment.ProcessPaymentUseCase
import com.tharsis.quickservices.utils.AppResult
import com.tharsis.quickservices.utils.Constants
import com.tharsis.quickservices.utils.InstallmentOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val cancelPaymentUseCase: CancelPaymentUseCase,
    private val getUserBookingsUseCase: GetUserBookingsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingId: String = checkNotNull(savedStateHandle[Constants.ARG_BOOKING_ID])

    private val _state = MutableStateFlow(PaymentState(bookingId = bookingId))
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    private var currentBooking: Booking? = null
    private var userEmail: String? = null

    init {
        loadBookingDetails()
    }

    private fun loadBookingDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getUserBookingsUseCase(userEmail!!).collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        val booking = result.data.find { it.id == bookingId }

                        if (booking != null) {
                            currentBooking = booking
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    amount = booking.servicePrice,
                                    serviceName = booking.serviceName,
                                    customerName = booking.customerName,
                                    customerEmail = booking.customerEmail,
                                    serviceDuration = booking.serviceDurationMinutes,
                                    errorMessage = null
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "Agendamento não encontrado"
                                )
                            }
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
                    is AppResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _state.update { it.copy(selectedPaymentMethod = method) }
    }

    fun selectInstallment(option: InstallmentOption) {
        _state.update { it.copy(selectedInstallment = option) }
    }

    fun processPayment() {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }

            val payment = Payment(
                id = Payment.generateId(),
                bookingId = bookingId,
                amount = state.value.amount,
                paymentMethod = state.value.selectedPaymentMethod,
                status = PaymentStatus.PROCESSING,
                createdAt = System.currentTimeMillis()
            )

            when (val result = processPaymentUseCase(payment, currentBooking!!)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            isSuccess = true,
                            paymentId = result.data.id
                        )
                    }
                }
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }
}