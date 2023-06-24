package dev.l3m4rk.ridango.tickets.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.l3m4rk.ridango.tickets.data.TicketsRepository
import dev.l3m4rk.ridango.tickets.data.TicketsRepositoryImpl

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
interface DataModule {

    @Binds
    fun bindRepository(repositoryImpl: TicketsRepositoryImpl): TicketsRepository
}