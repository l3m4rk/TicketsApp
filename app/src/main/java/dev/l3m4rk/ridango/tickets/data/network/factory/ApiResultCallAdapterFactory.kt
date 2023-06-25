package dev.l3m4rk.ridango.tickets.data.network.factory

import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type,
                     annotations: Array<out Annotation>,
                     retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            return null
        }

        val callTypeParameter = getParameterUpperBound(0, returnType)

        if (getRawType(callTypeParameter) != ApiResult::class.java) {
            return null
        }

        if (callTypeParameter !is ParameterizedType) {
            return null
        }

        val apiResultParameter = getParameterUpperBound(0, callTypeParameter)

        return ApiResultCallAdapter<Any>(apiResultParameter)
    }
}
