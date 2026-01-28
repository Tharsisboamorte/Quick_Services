package com.tharsis.quickservices.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tharsis.quickservices.data.model.BookingModel
import com.tharsis.quickservices.data.model.PaymentModel
import com.tharsis.quickservices.data.model.ServiceModel
import com.tharsis.quickservices.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton
import com.tharsis.quickservices.utils.AppResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    /**
     * Retrieves all services as a Flow for real-time updates.
     */
    fun getAllServices(): Flow<AppResult<List<ServiceModel>>> = callbackFlow {
        val listenerRegistration = firestore.collection(Constants.COLLECTION_SERVICES)
            .whereEqualTo("is_available", true)
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(AppResult.Error(error.message.toString()))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val services = snapshot.documents.mapNotNull { document ->
                        try {
                            document.toObject(ServiceModel::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(AppResult.Success(services))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }


    suspend fun getServiceById(serviceId: String): AppResult<ServiceModel> {
        return try {
            val document = firestore.collection(Constants.COLLECTION_SERVICES)
                .document(serviceId)
                .get()
                .await()

            val service = document.toObject(ServiceModel::class.java)
            if (service != null) {
                AppResult.Success(service)
            } else {
                AppResult.Error("Service not found")
            }
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }


    suspend fun createBooking(booking: BookingModel): AppResult<BookingModel> {
        return try {
            firestore.collection(Constants.COLLECTION_BOOKINGS)
                .document(booking.id)
                .set(booking)
                .await()

            AppResult.Success(booking)
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

    suspend fun getBookingById(bookingId: String): AppResult<BookingModel> {
        return try {
            val document = firestore.collection(Constants.COLLECTION_BOOKINGS)
                .document(bookingId)
                .get()
                .await()

            val booking = document.toObject(BookingModel::class.java)
            if (booking != null) {
                AppResult.Success(booking)
            } else {
                AppResult.Error("Booking not found")
            }
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }


    suspend fun updateBooking(booking: BookingModel): AppResult<BookingModel> {
        return try {
            firestore.collection(Constants.COLLECTION_BOOKINGS)
                .document(booking.id)
                .set(booking)
                .await()

            AppResult.Success(booking)
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

    fun getUserBookings(userEmail: String): Flow<AppResult<List<BookingModel>>> = callbackFlow {
        val listenerRegistration = firestore.collection(Constants.COLLECTION_BOOKINGS)
            .whereEqualTo("customer_email", userEmail)
            .orderBy("scheduled_timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(AppResult.Error(error.message.toString()))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val bookings = snapshot.documents.mapNotNull { document ->
                        try {
                            document.toObject(BookingModel::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(AppResult.Success(bookings))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Creates a payment record in Firestore.
     */
    suspend fun createPayment(payment: PaymentModel): AppResult<PaymentModel> {
        return try {
            firestore.collection(Constants.COLLECTION_PAYMENTS)
                .document(payment.id)
                .set(payment)
                .await()

            AppResult.Success(payment)
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

    /**
     * Updates an existing payment.
     */
    suspend fun updatePayment(payment: PaymentModel): AppResult<PaymentModel> {
        return try {
            firestore.collection(Constants.COLLECTION_PAYMENTS)
                .document(payment.id)
                .set(payment)
                .await()

            AppResult.Success(payment)
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

    /**
     * Retrieves a payment by ID.
     */
    suspend fun getPaymentById(paymentId: String): AppResult<PaymentModel> {
        return try {
            val document = firestore.collection(Constants.COLLECTION_PAYMENTS)
                .document(paymentId)
                .get()
                .await()

            val payment = document.toObject(PaymentModel::class.java)
            if (payment != null) {
                AppResult.Success(payment)
            } else {
                AppResult.Error("Payment not found")
            }
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }
}