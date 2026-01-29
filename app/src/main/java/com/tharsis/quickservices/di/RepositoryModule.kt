package com.tharsis.quickservices.di

import android.content.Context
import android.util.Log
import cielo.orders.domain.Credentials
import cielo.sdk.order.OrderManager
import cielo.sdk.order.ServiceBindListener
import com.tharsis.quickservices.data.network.FirebaseDataSource
import com.tharsis.quickservices.data.repository.CieloLioPaymentRepoImpl
import com.tharsis.quickservices.data.repository.ServiceRepoImpl
import com.tharsis.quickservices.domain.repository.CieloLioPaymentRepository
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

/**
 * Hilt module for repository bindings.
 */
@DelicateCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    const val TAG = "RepositoryModule"

    @Provides
    @Singleton
    fun provideCieloCredentials(): Credentials {
        return Credentials(
            accessToken = Constants.CIELO_ACCESS_TOKEN,
            clientID = Constants.CIELO_CLIENT_ID
        )
    }

    @Provides
    @Singleton
    fun provideServiceRepository(
        @ApplicationContext context: Context,
        firebaseDataSource: FirebaseDataSource
    ): ServiceRepository {
        return ServiceRepoImpl(
            context = context,
            firebaseDataSrc = firebaseDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideCieloPaymentRepository(
        @ApplicationContext context: Context,
        orderManager: OrderManager
    ): CieloLioPaymentRepository {
        return CieloLioPaymentRepoImpl(context,orderManager)
    }

    @Provides
    @Singleton
    fun provideOrderManager(
        @ApplicationContext context: Context,
        credentials: Credentials
    ): OrderManager {
        val orderManager = OrderManager(credentials, context)

        val bindChannel = Channel<Boolean>(1)

        val serviceBindListener = object : ServiceBindListener {
            override fun onServiceBound() {
                Log.d(TAG, "OrderManager vinculado com sucesso")
                runBlocking { bindChannel.send(true) }
            }

            override fun onServiceBoundError(throwable: Throwable) {
                Log.e(TAG, "Erro ao vincular OrderManager", throwable)
                runBlocking { bindChannel.send(false) }
            }

            override fun onServiceUnbound() {
                Log.d(TAG, "OrderManager desvinculado")
            }
        }

        orderManager.bind(context, serviceBindListener)

        return orderManager
    }
}