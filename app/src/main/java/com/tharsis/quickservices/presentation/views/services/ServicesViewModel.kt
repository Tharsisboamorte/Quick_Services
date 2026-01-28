package com.tharsis.quickservices.presentation.views.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tharsis.quickservices.domain.model.ServiceCategory
import com.tharsis.quickservices.domain.usecase.services.GetServiceByIdUseCase
import com.tharsis.quickservices.domain.usecase.services.GetServiceUseCase
import com.tharsis.quickservices.utils.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServiceUseCase,
    private val getServiceByIdUseCase: GetServiceByIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ServicesState())
    val state: StateFlow<ServicesState> = _state.asStateFlow()

    init {
        loadServices()
    }
   
    fun loadServices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getServicesUseCase().collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        _state.update {
                            it.copy(
                                services = result.data,
                                isLoading = false,
                                errorMessage = null
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
                    is AppResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun filterByCategory(category: ServiceCategory?) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }


    fun retry() {
        loadServices()
    }
}