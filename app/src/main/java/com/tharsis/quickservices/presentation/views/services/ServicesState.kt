package com.tharsis.quickservices.presentation.views.services

import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.model.ServiceCategory

data class ServicesState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: ServiceCategory? = null
) {

    fun getFilteredServices(): List<Service> {
        return if (selectedCategory != null) {
            services.filter { it.category == selectedCategory }
        } else {
            services
        }
    }

    fun isEmpty(): Boolean = services.isEmpty() && !isLoading

    fun hasError(): Boolean = errorMessage != null
}