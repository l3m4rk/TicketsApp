package dev.l3m4rk.ridango.tickets.data.network.model

sealed class ApiResult<T> {
    data class Success<T>(
        val code: Int,
        val data: T,
    ) : ApiResult<T>()

    data class ApiFailure<T>(
        val code: Int,
        val t: Throwable,
    ) : ApiResult<T>()

    data class NetworkFailure<T>(
        val t: Throwable,
    ) : ApiResult<T>()

    data class UnknownFailure<T>(
        val t: Throwable,
    ) : ApiResult<T>()
}
