package com.tharsis.quickservices.domain.model

data class Service(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val durationMinutes: Int,
    val category: ServiceCategory,
    val imageUrl: String? = null,
    val isAvailable: Boolean = false
) {
    fun getFormattedPrice(): String = "R$ %.2f".format(price)

    fun getFormattedDuration(): String {
        return when {
            durationMinutes < 60 -> "$durationMinutes min"
            durationMinutes % 60 == 0 -> "${durationMinutes / 60}h"
            else -> "${durationMinutes / 60}h ${durationMinutes % 60}min"
        }
    }
}

enum class ServiceCategory(val displayName: String) {
    BEAUTY("Beauty & Hair"),
    AUTOMOTIVE("Automotive"),
    CLEANING("Cleaning"),
    MAINTENANCE("Maintenance"),
    WELLNESS("Wellness"),
    OTHER("Other")
}