package dev.l3m4rk.ridango.tickets.data

import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.model.ApiResult
import dev.l3m4rk.ridango.tickets.data.network.TicketsApi
import dev.l3m4rk.ridango.tickets.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface TicketsRepository {

    suspend fun sendTicket(ticket: Ticket): Result<Ticket>

}

@Singleton
class TicketsRepositoryImpl @Inject constructor(
    private val ticketsApi: TicketsApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TicketsRepository {

    override suspend fun sendTicket(ticket: Ticket): Result<Ticket> {
        return withContext(ioDispatcher) {
            when (val result = ticketsApi.createTicket(ticket)) {
                is ApiResult.Success -> Result.success(result.data)
                is ApiResult.ApiFailure -> Result.failure(result.t)
                is ApiResult.NetworkFailure -> Result.failure(result.t)
                is ApiResult.UnknownFailure -> Result.failure(result.t)
            }
        }
    }
}
