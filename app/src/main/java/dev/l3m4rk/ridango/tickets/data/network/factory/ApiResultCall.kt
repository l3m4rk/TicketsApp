package dev.l3m4rk.ridango.tickets.data.network.factory

import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiResultCall<T>(private val wrappedCall: Call<T>) : Call<ApiResult<T>> {

    override fun clone(): Call<ApiResult<T>> {
        return ApiResultCall(wrappedCall)
    }

    override fun execute(): Response<ApiResult<T>> {
        return try {
            Response.success(wrappedCall.execute().process())
        } catch (t: Throwable) {
            Response.success(t.process())
        }
    }

    private fun Response<T>.process(): ApiResult<T> {
        return when {
            isSuccessful -> {
                body()?.let { ApiResult.Success(code(), it) }
                    ?: ApiResult.UnknownFailure(Throwable("Body is missing"))
            }

            else -> ApiResult.ApiFailure(code())
        }
    }

    private fun Throwable.process(): ApiResult<T> {
        return when (this) {
            is IOException -> ApiResult.NetworkFailure(this)
            else -> ApiResult.UnknownFailure(this)
        }
    }

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        wrappedCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(this@ApiResultCall, Response.success(response.process()))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ApiResultCall, Response.success(t.process()))
            }
        })
    }

    override fun isExecuted(): Boolean = isExecuted

    override fun cancel() = wrappedCall.cancel()

    override fun isCanceled(): Boolean = isCanceled

    override fun request(): Request = wrappedCall.request()

    override fun timeout(): Timeout = wrappedCall.timeout()
}