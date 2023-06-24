package dev.l3m4rk.ridango.tickets.data.network

import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import retrofit2.http.Body
import retrofit2.http.POST

interface TicketsApi {

    @POST("/")
    suspend fun createTicket(@Body ticket: Ticket): ApiResult<Ticket>
}
