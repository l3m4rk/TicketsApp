package dev.l3m4rk.ridango.tickets.util.core

/**
 * Used instead of [kotlin.Result] because it still has problems with mockk
 */
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val t: Throwable) : Result<T>()
}
