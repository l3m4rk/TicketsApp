package dev.l3m4rk.ridango.tickets.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.l3m4rk.ridango.tickets.data.TicketsRepository
import dev.l3m4rk.ridango.tickets.data.TicketsRepositoryImpl
import dev.l3m4rk.ridango.tickets.domain.CreateTicketUseCase
import dev.l3m4rk.ridango.tickets.domain.CreateTicketUseCaseImpl
import dev.l3m4rk.ridango.tickets.domain.SanitizePriceInputUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizePriceInputUseCaseImpl

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
interface BuyTicketModule {

    @Binds
    fun bindRepository(repositoryImpl: TicketsRepositoryImpl): TicketsRepository

    @Binds
    fun bindSanitizePriceInput(impl: SanitizePriceInputUseCaseImpl): SanitizePriceInputUseCase

    @Binds
    fun bindCreateTicket(impl: CreateTicketUseCaseImpl): CreateTicketUseCase
}