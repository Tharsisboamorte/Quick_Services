package com.tharsis.quickservices.utils

object Constants {

    const val COLLECTION_SERVICES = "services"
    const val COLLECTION_BOOKINGS = "bookings"
    const val COLLECTION_PAYMENTS = "payments"

    // Navigation Routes
    const val ROUTE_SERVICES = "services"
    const val ROUTE_BOOKING = "booking/{serviceId}"
    const val ROUTE_PAYMENT = "payment/{bookingId}"
    const val ROUTE_CONFIRMATION = "confirmation/{bookingId}"

    // Navigation Arguments
    const val ARG_SERVICE_ID = "serviceId"
    const val ARG_BOOKING_ID = "bookingId"

    // Date/Time Formats
    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy"
    const val TIME_FORMAT_DISPLAY = "HH:mm"
    const val DATETIME_FORMAT_FULL = "dd/MM/yyyy HH:mm"
    const val DATETIME_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss"

    // Payment
    const val PAYMENT_PROVIDER_CIELO = "CIELO_LIO"
    const val PAYMENT_METHOD_CREDIT = "CREDIT"
    const val PAYMENT_METHOD_DEBIT = "DEBIT"

    // Booking Status
    const val STATUS_PENDING = "PENDING"
    const val STATUS_CONFIRMED = "CONFIRMED"
    const val STATUS_CANCELLED = "CANCELLED"
    const val STATUS_COMPLETED = "COMPLETED"

    // Error Messages
    const val ERROR_NETWORK = "Network error. Please check your connection."
    const val ERROR_GENERIC = "An error occurred. Please try again."
    const val ERROR_PAYMENT_FAILED = "Payment failed. Please try again."
    const val ERROR_BOOKING_FAILED = "Failed to create booking. Please try again."
    const val ERROR_INVALID_DATE = "Please select a valid date and time."

    // Success Messages
    const val SUCCESS_BOOKING_CREATED = "Booking created successfully!"
    const val SUCCESS_PAYMENT_COMPLETED = "Payment completed successfully!"

    // Calendar Integration
    const val CALENDAR_EVENT_REMINDER_MINUTES = 30

    // Timeouts
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val PAYMENT_TIMEOUT_SECONDS = 60L

}