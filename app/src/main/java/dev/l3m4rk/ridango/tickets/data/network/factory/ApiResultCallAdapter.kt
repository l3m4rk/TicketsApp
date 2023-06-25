package dev.l3m4rk.ridango.tickets.data.network.factory

import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResultCallAdapter<T>(
    private val type: Type,
) : CallAdapter<T, Call<ApiResult<T>>>{

    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<ApiResult<T>> {
        return ApiResultCall(call)
    }
}
