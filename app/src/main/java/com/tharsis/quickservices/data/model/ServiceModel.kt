package com.tharsis.quickservices.data.model

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.tharsis.quickservices.domain.model.Service
import com.tharsis.quickservices.domain.model.ServiceCategory

    data class ServiceModel(
        @DocumentId
        val id: String = "",
        @PropertyName("name")
        val name: String = "",
        @PropertyName("description")
        val description: String = "",
        @PropertyName("price")
        val price: Double = 0.0,
        @PropertyName("duration_minutes")
        val durationMinutes: Int = 0,
        @PropertyName("category")
        val category: String = "",
        @PropertyName("image_url")
        val imageUrl: String? = null,
        @PropertyName("is_available")
        val isAvailable: Boolean = true
    ) {
        fun toDomain(): Service {
            return Service(
                id = id,
                name = name,
                description = description,
                price = price,
                durationMinutes = durationMinutes,
                category = parseCategoryFromString(category),
                imageUrl = imageUrl,
                isAvailable = isAvailable
            )
        }

    private fun parseCategoryFromString(categoryString: String): ServiceCategory {
        return try {
            ServiceCategory.valueOf(categoryString.uppercase())
        } catch (e: Exception) {
            Log.e(TAG, "parse category Error: ${e.message}", e)
            ServiceCategory.OTHER
        }
    }

        companion object {
           const val TAG = "ServiceModel"

        fun fromDomain(service: Service): ServiceModel {
            return ServiceModel(
                id = service.id,
                name = service.name,
                description = service.description,
                price = service.price,
                durationMinutes = service.durationMinutes,
                category = service.category.name,
                imageUrl = service.imageUrl,
                isAvailable = service.isAvailable
            )
        }
    }
}

