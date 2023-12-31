package dev.l3m4rk.ridango.tickets.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.l3m4rk.ridango.tickets.data.network.FakeWebServer
import dev.l3m4rk.ridango.tickets.data.network.FakeWebServerImpl
import dev.l3m4rk.ridango.tickets.data.network.TicketsApi
import dev.l3m4rk.ridango.tickets.data.network.factory.ApiResultCallAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.protobuf.ProtoConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @IntoSet
    @Provides
    fun loggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private const val BASE_URL = "http://0.0.0.0:8080/api/v1/"

    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 60L
    private const val WRITE_TIMEOUT = 100L

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .apply {
                interceptors.forEach {
                    addInterceptor(it)
                }
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ProtoConverterFactory.create())
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .client(client)
            .build()
    }

    @Provides
    @Reusable
    fun provideApi(retrofit: Retrofit): TicketsApi {
        return retrofit.create(TicketsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWebServer(@IoDispatcher dispatcher: CoroutineDispatcher): FakeWebServer {
        return FakeWebServerImpl(dispatcher)
    }
}
