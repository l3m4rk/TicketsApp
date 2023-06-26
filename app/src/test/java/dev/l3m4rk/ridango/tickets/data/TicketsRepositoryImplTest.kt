package dev.l3m4rk.ridango.tickets.data

import com.google.common.truth.Truth.assertThat
import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.data.network.FakeWebServer
import dev.l3m4rk.ridango.tickets.data.network.TicketsApi
import dev.l3m4rk.ridango.tickets.data.network.model.ApiException
import dev.l3m4rk.ridango.tickets.data.network.model.ApiResult
import dev.l3m4rk.ridango.tickets.util.core.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class TicketsRepositoryImplTest {

    private val api = mockk<TicketsApi>()
    private val webServer = mockk<FakeWebServer>() {
        every { enqueueTicketResponse(any(), any()) } returns Unit
    }

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private val repository = TicketsRepositoryImpl(api, webServer, testDispatcher)

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

        assertThat(result).isInstanceOf(Result.Success::class.java)
        val data = (result as Result.Success).data
        assertThat(data).isEqualTo(ticket)

        coVerify { api.createTicket(any()) }
    }

    @Test
    fun `create ticket when api fails with ApiFailure`() = runTest(testDispatcher) {
        coEvery { api.createTicket(any()) } returns ApiResult.ApiFailure(500)

        val result: Result<Ticket> = repository.sendTicket(ticket)

        coVerify(exactly = 1) { api.createTicket(eq(ticket)) }

        assertThat(result).isInstanceOf(Result.Error::class.java)
        val t = (result as Result.Error).t
        assertThat(t).isInstanceOf(ApiException::class.java)
        assertThat((t as ApiException).code).isEqualTo(500)
    }

    @Test
    fun `create ticket when api fails with Network Failure`() = runTest(testDispatcher) {
        coEvery { api.createTicket(any()) } returns ApiResult.NetworkFailure(IOException("No Connection"))

        val result: Result<Ticket> = repository.sendTicket(ticket)

        coVerify(exactly = 1) { api.createTicket(eq(ticket)) }

        assertThat(result).isInstanceOf(Result.Error::class.java)
        val t = (result as Result.Error).t
        assertThat(t).isInstanceOf(IOException::class.java)
        assertThat(t.message).isEqualTo("No Connection")
    }

    @Test
    fun `create ticket when api fails with Unknown Failure`() = runTest(testDispatcher) {
        val message = "Something went wrong"
        coEvery { api.createTicket(any()) } returns ApiResult.NetworkFailure(Throwable(message))

        val result: Result<Ticket> = repository.sendTicket(ticket)

        coVerify(exactly = 1) { api.createTicket(eq(ticket)) }

        assertThat(result).isInstanceOf(Result.Error::class.java)
        val t = (result as Result.Error).t
        assertThat(t).isInstanceOf(Throwable::class.java)
        assertThat(t.message).isEqualTo(message)
    }
}