package dev.l3m4rk.ridango.tickets.data

import com.google.common.truth.Truth.assertThat
import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import dev.l3m4rk.ridango.tickets.data.network.TicketsApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TicketsRepositoryImplTest {

    private val api = mockk<TicketsApi>()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val repository = TicketsRepositoryImpl(api, testDispatcher)

    private val ticket = Ticket.newBuilder()
        .setId(1)
        .setProductName("Test Product")
        .setPrice(23)
        .build()

    private val successResult = ApiResult.Success(
        code = 200,
        data = ticket
    )

    @Test
    fun `create ticket when api return success`() = runTest(testDispatcher) {
        coEvery { api.createTicket(any()) } returns successResult

        val result: Result<Ticket> = repository.sendTicket(ticket)

        assertThat(result.isSuccess).isTrue()
        val data = result.getOrNull()
        assertThat(data).isNotNull()
        assertThat(data!!).isEqualTo(ticket)

        coVerify { api.createTicket(any()) }
    }
}