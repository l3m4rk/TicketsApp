package dev.l3m4rk.ridango.tickets.domain

import com.google.common.truth.Truth.assertThat
import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.TicketsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CreateTicketUseCaseTest {

    private val repository = mockk<TicketsRepository>()
    private val createTicketUseCase = CreateTicketUseCaseImpl(repository)

    @Test
    fun `ticket created successfully`() = runTest {
        val productName = "Product 1"
        val price = 350 // 3.5 EUR
        val successTicketResponse = Ticket.newBuilder()
            .setPrice(price)
            .setProductName(productName)
            .build()

        coEvery { repository.sendTicket(any()) } returns Result.success(successTicketResponse)

        val result = createTicketUseCase(productName, price)

        coVerify(exactly = 1) { repository.sendTicket(any()) }
        assertThat(result.isSuccess).isTrue()
        result.getOrNull()?.also { ticket ->
            assertThat(ticket.id).isEqualTo(0)
            assertThat(ticket.productName).isEqualTo(productName)
            assertThat(ticket.price).isEqualTo(price)
        }
    }
}