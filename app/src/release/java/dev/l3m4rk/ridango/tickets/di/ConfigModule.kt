package dev.l3m4rk.ridango.tickets.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.l3m4rk.ridango.tickets.data.logging.ReleaseLogger
import timber.log.Timber
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ConfigModule {

    @Singleton
    @Provides
    fun provideTimberTree(): Timber.Tree = ReleaseLogger()
}
