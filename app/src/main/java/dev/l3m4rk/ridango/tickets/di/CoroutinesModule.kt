package dev.l3m4rk.ridango.tickets.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Retention
@Qualifier
annotation class IoDispatcher
