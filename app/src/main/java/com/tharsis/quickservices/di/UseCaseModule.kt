package com.tharsis.quickservices.di

import com.tharsis.quickservices.domain.repository.CieloLioPaymentRepository
import com.tharsis.quickservices.domain.repository.ServiceRepository
import com.tharsis.quickservices.domain.usecase.booking.CreateBookingUseCase
import com.tharsis.quickservices.domain.usecase.booking.GetBookingUseCase
import com.tharsis.quickservices.domain.usecase.booking.GetUserBookingsUseCase
import com.tharsis.quickservices.domain.usecase.booking.UpdateBookingUseCase
import com.tharsis.quickservices.domain.usecase.payment.CancelPaymentUseCase
import com.tharsis.quickservices.domain.usecase.payment.ProcessPaymentUseCase
import com.tharsis.quickservices.domain.usecase.services.GetServiceByIdUseCase
import com.tharsis.quickservices.domain.usecase.services.GetServiceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    // Service Use Cases
    @Provides
    fun provideGetServiceUseCase(
        repository: ServiceRepository
    ): GetServiceUseCase {
        return GetServiceUseCase(repository)
    }

    @Provides
    fun provideGetServiceByIdUseCase(
        repository: ServiceRepository
    ): GetServiceByIdUseCase {
        return GetServiceByIdUseCase(repository)
    }

    // Payment Use Cases
    @Provides
    fun provideCancelPaymentUseCase(
        repository: CieloLioPaymentRepository
    ): CancelPaymentUseCase {
        return CancelPaymentUseCase(repository)
    }

    @Provides
    fun provideProcessPaymentUseCase(
        repository: CieloLioPaymentRepository
    ): ProcessPaymentUseCase {
        return ProcessPaymentUseCase(repository)
    }

    // Booking Use Cases
    @Provides
    fun provideCreateBookingUseCase(
        repository: ServiceRepository
    ): CreateBookingUseCase {
        return CreateBookingUseCase(repository)
    }

    @Provides
    fun provideGetBookingUseCase(
        repository: ServiceRepository
    ): GetBookingUseCase {
        return GetBookingUseCase(repository)
    }

    @Provides
    fun provideGetUserBookingsUseCase(
        repository: ServiceRepository
    ): GetUserBookingsUseCase {
        return GetUserBookingsUseCase(repository)
    }

    @Provides
    fun provideUpdateBookingUseCase(
        repository: ServiceRepository
    ): UpdateBookingUseCase {
        return UpdateBookingUseCase(repository)
    }

}