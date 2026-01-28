package com.tharsis.quickservices.utils

/**
 * A generic wrapper for handling success and error states throughout the app.
 */
sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error<T>(val message: String, val data: T? = null) : AppResult<T>()
    object Loading : AppResult<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isLoading: Boolean
        get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun exceptionOrNull(): Exception? = when (this) {
        is Error -> Exception(message)
        else -> null
    }
}

/**
 * Extension function to map Result data to another type.
 */
inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Error -> AppResult.Error(message, data?.let(transform))
        is AppResult.Loading -> AppResult.Loading
    }
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (String) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(message)
    return this
}
