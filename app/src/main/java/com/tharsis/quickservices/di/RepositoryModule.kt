package com.tharsis.quickservices.di

import com.tharsis.quickservices.data.repository.ServiceRepoImpl
import com.tharsis.quickservices.domain.repository.ServiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Hilt module for repository bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindServiceRepository(
        serviceRepositoryImpl: ServiceRepoImpl
    ): ServiceRepository
}