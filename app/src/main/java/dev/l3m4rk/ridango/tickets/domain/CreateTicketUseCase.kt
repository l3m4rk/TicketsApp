package dev.l3m4rk.ridango.tickets.domain

import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import javax.inject.Inject

interface CreateTicketUseCase {
    operator fun invoke(productName: String, price: Int): Ticket
}

class CreateTicketUseCaseImpl @Inject constructor() : CreateTicketUseCase {
    override fun invoke(productName: String, price: Int): Ticket {
        return Ticket.newBuilder()
            .setProductName(productName)
            .setPrice(price)
            .build()
    }
}
