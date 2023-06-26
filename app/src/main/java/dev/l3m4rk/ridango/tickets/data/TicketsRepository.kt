package dev.l3m4rk.ridango.tickets.data

import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.network.FakeWebServer
import dev.l3m4rk.ridango.tickets.data.network.TicketsApi
import dev.l3m4rk.ridango.tickets.data.network.model.ApiException
import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import dev.l3m4rk.ridango.tickets.di.IoDispatcher
import dev.l3m4rk.ridango.tickets.util.core.Result
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
    private val webServer: FakeWebServer,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TicketsRepository {

    override suspend fun sendTicket(ticket: Ticket): Result<Ticket> {
        // STOPSHIP: only for developing purposes
        webServer.enqueueTicketResponse(ticket.productName, ticket.price)

        return withContext(ioDispatcher) {
            when (val result = ticketsApi.createTicket(ticket)) {
                is ApiResult.Success -> Result.Success(result.data)
                is ApiResult.ApiFailure -> Result.Error(ApiException(result.code))
                is ApiResult.NetworkFailure -> Result.Error(result.t)
                is ApiResult.UnknownFailure -> Result.Error(result.t)
            }
        }
    }
}
