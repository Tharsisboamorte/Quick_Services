package com.tharsis.quickservices.di

import android.content.Context
import com.tharsis.quickservices.data.network.FirebaseDataSource
import com.tharsis.quickservices.data.repository.ServiceRepoImpl
import com.tharsis.quickservices.domain.repository.ServiceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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
}