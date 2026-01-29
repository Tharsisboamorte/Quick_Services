package com.tharsis.quickservices.utils

import com.tharsis.quickservices.BuildConfig

object Constants {

    const val COLLECTION_SERVICES = "services"
    const val COLLECTION_BOOKINGS = "bookings"
    const val COLLECTION_PAYMENTS = "payments"

    // Navigation Routes

    // Navigation Arguments
    const val ARG_SERVICE_ID = "serviceId"
    const val ARG_BOOKING_ID = "bookingId"

    // Date/Time Formats
    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy"
    const val TIME_FORMAT_DISPLAY = "HH:mm"

    //Cielo
    const val CIELO_CLIENT_ID = BuildConfig.CIELO_CLIENT_ID
    const val CIELO_ACCESS_TOKEN = BuildConfig.CIELO_ACCESS_TOKEN
    const val CIELO_MERCHANT_ID = BuildConfig.CIELO_MERCHANT_ID
    const val API_URL = BuildConfig.API_URL

    const val ERROR_PAYMENT_FAILED = "Payment failed. Please try again."

}