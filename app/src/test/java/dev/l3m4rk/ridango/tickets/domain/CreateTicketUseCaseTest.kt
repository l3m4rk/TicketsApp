package dev.l3m4rk.ridango.tickets.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CreateTicketUseCaseTest {

    private val createTicketUseCase = CreateTicketUseCaseImpl()

    @Test
    fun `create ticket works`() {
        val productName = "Product 1"
        val price = 350 // 3.5 EUR

        val expectedPrice = 350

        val ticket = createTicketUseCase(productName, price)

        assertThat(ticket.id).isEqualTo(0)
        assertThat(ticket.productName).isEqualTo(productName)
        assertThat(ticket.price).isEqualTo(expectedPrice)
    }
}