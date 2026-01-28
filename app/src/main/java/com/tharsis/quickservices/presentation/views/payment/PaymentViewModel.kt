package com.tharsis.quickservices.presentation.views.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.PaymentMethod
import com.tharsis.quickservices.domain.usecase.payment.GetPaymentUseCase
import com.tharsis.quickservices.domain.usecase.payment.ProcessPaymentUseCase
import com.tharsis.quickservices.utils.AppResult
import com.tharsis.quickservices.utils.Constants
import com.tharsis.quickservices.utils.InstallmentOption
import com.tharsis.quickservices.utils.PaymentUtil
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
    private val getPaymentUseCase: GetPaymentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingId: String = checkNotNull(savedStateHandle[Constants.ARG_BOOKING_ID])

    private val _state = MutableStateFlow(PaymentState(bookingId = bookingId))
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    fun setAmount(amount: Double) {
        _state.update {
            it.copy(
                amount = amount,
                installmentOptions = PaymentUtil.getInstallmentOptions(amount),
                selectedInstallment = PaymentUtil.getInstallmentOptions(amount).firstOrNull()
            )
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _state.update { it.copy(selectedPaymentMethod = method) }
    }

    fun selectInstallment(option: InstallmentOption) {
        _state.update { it.copy(selectedInstallment = option) }
    }

    fun processPayment(payment: Payment) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, errorMessage = null) }

            when (val result = processPaymentUseCase( payment = payment)) {
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