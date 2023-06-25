package dev.l3m4rk.ridango.tickets.domain

import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.TicketsRepository
import dev.l3m4rk.ridango.tickets.util.core.Result
import javax.inject.Inject

interface CreateTicketUseCase {
    suspend operator fun invoke(productName: String, price: Int): Result<Ticket>
}

class CreateTicketUseCaseImpl @Inject constructor(
    private val repository: TicketsRepository,
) : CreateTicketUseCase {
    override suspend fun invoke(productName: String, price: Int): Result<Ticket> {
        val ticket = Ticket.newBuilder()
            .setProductName(productName)
            .setPrice(price)
            .build()
        return repository.sendTicket(ticket)
    }
}
