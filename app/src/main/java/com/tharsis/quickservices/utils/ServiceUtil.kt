package com.tharsis.quickservices.utils

import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.model.ServiceCategory

object ServiceUtil {
    fun filterByCategory(
        services: List<Service>,
        category: ServiceCategory
    ): List<Service> {
        return services.filter { it.category == category }
    }

    fun filterAvailable(services: List<Service>): List<Service> {
        return services.filter { it.isAvailable }
    }

    fun sortByPriceAscending(services: List<Service>): List<Service> {
        return services.sortedBy { it.price }
    }

    fun sortByPriceDescending(services: List<Service>): List<Service> {
        return services.sortedByDescending { it.price }
    }

    fun sortByName(services: List<Service>): List<Service> {
        return services.sortedBy { it.name }
    }

    fun sortByDuration(services: List<Service>): List<Service> {
        return services.sortedBy { it.durationMinutes }
    }

    fun searchServices(
        services: List<Service>,
        query: String
    ): List<Service> {
        if (query.isBlank()) return services

        val lowerQuery = query.lowercase()
        return services.filter { service ->
            service.name.lowercase().contains(lowerQuery) ||
                    service.description.lowercase().contains(lowerQuery)
        }
    }

    fun filterByPriceRange(
        services: List<Service>,
        minPrice: Double,
        maxPrice: Double
    ): List<Service> {
        return services.filter { it.price in minPrice..maxPrice }
    }

    fun filterByDurationRange(
        services: List<Service>,
        minMinutes: Int,
        maxMinutes: Int
    ): List<Service> {
        return services.filter { it.durationMinutes in minMinutes..maxMinutes }
    }
}