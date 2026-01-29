package com.tharsis.quickservices.data.repository

import android.content.Context
import android.util.Log
import cielo.orders.domain.CheckoutRequest
import cielo.orders.domain.Order
import cielo.orders.domain.product.PrimaryProduct
import cielo.orders.domain.product.SecondaryProduct
import cielo.sdk.order.OrderManager
import cielo.sdk.order.ServiceBindListener
import cielo.sdk.order.cancellation.CancellationListener
import cielo.sdk.order.payment.PaymentError
import cielo.sdk.order.payment.PaymentListener
import com.tharsis.quickservices.domain.model.Booking
import com.tharsis.quickservices.domain.model.Payment
import com.tharsis.quickservices.domain.model.PaymentMethod
import com.tharsis.quickservices.domain.model.PaymentStatus
import com.tharsis.quickservices.domain.repository.CieloLioPaymentRepository
import com.tharsis.quickservices.utils.AppResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.ArrayList
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import cielo.sdk.order.payment.Payment as cieloPayment

@DelicateCoroutinesApi
@Singleton
class CieloLioPaymentRepoImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val orderManager: OrderManager
) : CieloLioPaymentRepository {

    private val mutex = Mutex()
    private var isServiceReady = false
    private val pendingActions = mutableListOf<() -> Unit>()
    private val activePayments = mutableMapOf<String, cieloPayment>()


    init {
        setupServiceListener()
    }

    private fun setupServiceListener() {
        val listener = object : ServiceBindListener {
            override fun onServiceBound() {
                Log.d(TAG, "Serviço vinculado e pronto para uso")
                isServiceReady = true
                executePendingActions()
            }

            override fun onServiceBoundError(throwable: Throwable) {
                Log.e(TAG, "Erro ao vincular serviço", throwable)
                isServiceReady = false
            }

            override fun onServiceUnbound() {
                Log.d(TAG, "Serviço desvinculado")
                isServiceReady = false
            }
        }

        try {
            if (!isServiceReady) {
                orderManager.bind(context, listener)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao tentar vincular serviço", e)
        }

    }

    private fun executePendingActions() {
        pendingActions.forEach { action ->
            try {
                action.invoke()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao executar ação pendente", e)
            }
        }
        pendingActions.clear()
    }

    private suspend fun <T> executeWhenReady(action: () -> T): T {
        return mutex.withLock {
            if (isServiceReady) {
                action()
            } else {
                suspendCancellableCoroutine { continuation ->
                    pendingActions.add {
                        try {
                            continuation.resume(action()) { cause, _, _ -> null?.let { it(cause) } }
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                }
            }
        }
    }

    override suspend fun processPayment(payment: Payment, booking: Booking): AppResult<Payment>  =
        suspendCancellableCoroutine { continuation ->

            GlobalScope.launch {
                try {
                    executeWhenReady {
                        val order = createCieloOrder(payment, booking)

                        val product = PrimaryProduct(
                            id = UUID.randomUUID().timestamp(),
                            code = "service_${booking.serviceId}",
                            name = booking.serviceName,
                            secondaryProducts = ArrayList<SecondaryProduct>()
                        )
                        order.addItem(
                            sku = product.id.toString(),
                            name = product.name,
                            unitPrice = payment.amount.toLong(),
                            quantity = 1,
                            unitOfMeasure = "UNIDADE")

                        orderManager.updateOrder(order)
                        orderManager.placeOrder(order)

                        val paymentListener = object : PaymentListener {
                            override fun onCancel() {
                                Log.d(TAG, "Pagamento cancelado pelo usuário")
                                val cancelledPayment = payment.copy(
                                    status = PaymentStatus.CANCELLED,
                                    errorMessage = "Pagamento cancelado pelo usuário"
                                )
                                continuation.resume(AppResult.Success(cancelledPayment))
                            }

                            override fun onError(error: PaymentError) {
                                Log.e(TAG, "Erro no pagamento: ${error.description}")
                                continuation.resume(
                                    AppResult.Error(
                                        message = error.description
                                    )
                                )
                            }

                            override fun onPayment(order: Order) {
                                Log.d(TAG, "Pagamento realizado com sucesso: ${order.id}")

                                order.markAsPaid()
                                orderManager.updateOrder(order)

                                val successfulPayment = payment.copy(
                                    status = PaymentStatus.COMPLETED,
                                    transactionId = order.id,
                                    cieloOrderId = order.id,
                                    completedAt = System.currentTimeMillis()
                                )

                                continuation.resume(AppResult.Success(successfulPayment))
                            }

                            override fun onStart() {
                                Log.d(TAG, "Pagamento iniciado")
                            }
                        }

                        val checkoutRequest = CheckoutRequest.Builder()
                            .amount(booking.servicePrice.toLong())
                            .email(booking.customerEmail)
                            .build()

                        when (payment.paymentMethod) {
                            PaymentMethod.CREDIT, PaymentMethod.DEBIT, PaymentMethod.PIX -> {
                                orderManager.checkoutOrder(
                                    checkoutRequest, paymentListener
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao processar pagamento", e)
                    continuation.resume(
                        AppResult.Error(
                            message = e.message ?: "Erro desconhecido ao processar pagamento"
                        )
                    ) { cause, _, _ -> null
                        (cause)
                    }
                }
            }
        }

    override suspend fun cancelPayment(orderId: String): AppResult<Unit> =
        suspendCancellableCoroutine { continuation ->
            GlobalScope.launch {
                try {
                    executeWhenReady {
                        val cieloPayment = activePayments[orderId]

                        if (cieloPayment == null) {
                            Log.e(TAG, "Payment não encontrado para cancelamento: $orderId")
                            continuation.resume(
                                AppResult.Error(
                                    message = "Pagamento não encontrado para cancelamento")
                            )
                            return@executeWhenReady
                        }

                        val cancellationListener = object : CancellationListener {
                            override fun onSuccess(canceledOrder: Order) {
                                Log.d(TAG, "Cancelamento realizado com sucesso: $orderId")
                                activePayments.remove(orderId)
                                continuation.resume(AppResult.Success(Unit))
                            }

                            override fun onCancel() {
                                Log.d(TAG, "Cancelamento foi cancelado pelo usuário")
                                continuation.resume(
                                    AppResult.Error(
                                        message = "Cancelamento foi cancelado")
                                )
                            }

                            override fun onError(error: PaymentError) {
                                Log.e(TAG, "Erro ao cancelar: ${error.description}")
                                continuation.resume(
                                    AppResult.Error( error.description)
                                )
                            }
                        }

                        orderManager.cancelOrder(
                            context = context,
                            orderId = orderId,
                            payment = cieloPayment,
                            cancellationListener = cancellationListener
                        )

                        Log.d(TAG, "Solicitação de cancelamento enviada para: $orderId")
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao cancelar pedido: $orderId", e)
                    continuation.resume(
                        AppResult.Error(
                            message = "Erro ao cancelar pagamento"
                        )
                    )
                }
            }
        }

    private fun createCieloOrder(payment: Payment, booking: Booking): Order {
        val orderReference = "quickserve_${payment.bookingId}_${System.currentTimeMillis()}"
        val order = orderManager.createDraftOrder(orderReference)

        order?.notes = buildString {
            append("Cliente: ${booking.customerName}\n")
            append("Email: ${booking.customerEmail}\n")
            append("Telefone: ${booking.customerPhone}\n")
            append("Serviço: ${booking.serviceName}\n")
            if (booking.notes != null) {
                append("Obs: ${booking.notes}")
            }
        }

        Log.d(TAG, "📝 Order criada: $orderReference")
        return order!!
    }

    fun cleanup() {
        try {
            orderManager.unbind()
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao desvincular serviço", e)
        }
    }

    companion object {
        private const val TAG = "CieloLioPaymentManager"
    }
}