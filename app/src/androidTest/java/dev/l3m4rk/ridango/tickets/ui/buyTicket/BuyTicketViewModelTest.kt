package dev.l3m4rk.ridango.tickets.ui.buyTicket

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.l3m4rk.ridango.tickets.TicketOuterClass
import dev.l3m4rk.ridango.tickets.data.network.model.ApiException
import dev.l3m4rk.ridango.tickets.domain.CreateTicketUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizePriceInputUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizeProductNameInputUseCase
import dev.l3m4rk.ridango.tickets.domain.ValidateTicketInputUseCase
import dev.l3m4rk.ridango.tickets.util.core.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class BuyTicketViewModelTest {

    private val validateTicketInput = ValidateTicketInputUseCase()
    private val sanitizePriceInput = mockk<SanitizePriceInputUseCase>()
    private val sanitizeProductNameInput = mockk<SanitizeProductNameInputUseCase>()
    private val createTicket = mockk<CreateTicketUseCase>()

    private val testScheduler = TestCoroutineScheduler()

    private val priceInCents = 2300
    private val successTicketResponse = TicketOuterClass.Ticket.newBuilder()
        .setId(1)
        .setProductName("Ticket")
        .setPrice(priceInCents)
        .build()

    private lateinit var viewModel: BuyTicketViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        viewModel = BuyTicketViewModel(
            validateTicketInput = validateTicketInput,
            sanitizePriceInput = sanitizePriceInput,
            sanitizeProductNameInput = sanitizeProductNameInput,
            createTicket = createTicket,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_inputFieldsAreEmptyAndButtonDisabled() {
        val uiState = viewModel.uiState.value

        Truth.assertThat(uiState.buttonEnabled).isFalse()
        Truth.assertThat(uiState.productName).isEmpty()
        Truth.assertThat(uiState.price).isEmpty()

        Truth.assertThat(viewModel.buyTicketState.value).isEqualTo(BuyTicketState.Init)
    }

    @Test
    fun productInfoFilled_buttonEnabled() = runTest {
        Assert.assertFalse(viewModel.uiState.value.buttonEnabled)

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("32") // input in EUR

        viewModel.uiState.test {
            Assert.assertTrue(awaitItem().buttonEnabled)
        }
    }

    @Test
    fun enterInfo_SuccessfulTicketCreated() = runTest {

        coEvery {
            createTicket(any(), any())
        } returns Result.Success(successTicketResponse)

        every { sanitizePriceInput(any()) } returns priceInCents
        every { sanitizeProductNameInput(any()) } returns "Ticket"

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("23") // input in EUR
        viewModel.buyTicket()

        verify(exactly = 1) { sanitizePriceInput("23") }
        verify(exactly = 1) { sanitizeProductNameInput("Ticket") }

        viewModel.buyTicketState.test {
            val state = awaitItem()
            Truth.assertThat(state).isInstanceOf(BuyTicketState.Success::class.java)
            Assert.assertEquals(
                (state as BuyTicketState.Success).message,
                "Ticket id=${successTicketResponse.id} name=${successTicketResponse.productName} price=${successTicketResponse.price} created"
            )
        }
    }

    @Test
    fun enterInfo_createTicket_NetworkError() = runTest {
        coEvery {
            createTicket(any(), any())
        } returns Result.Error(IOException("No connection"))

        every { sanitizePriceInput(any()) } returns 4400
        every { sanitizeProductNameInput(any()) } returns "Ticket"

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("44") // input in EUR
        viewModel.buyTicket()

        verify(exactly = 1) { sanitizePriceInput("44") }
        verify(exactly = 1) { sanitizeProductNameInput("Ticket") }

        viewModel.buyTicketState.test {
            val state = awaitItem()
            Truth.assertThat(state).isInstanceOf(BuyTicketState.Error::class.java)
            val err = state as BuyTicketState.Error
            Assert.assertEquals(err.errorMessage, ErrorMessage.NetworkError)
        }
    }

    @Test
    fun enterInfo_createTicket_ServerError() = runTest {
        coEvery {
            createTicket(any(), any())
        } returns Result.Error(ApiException(code = 500))

        every { sanitizePriceInput(any()) } returns 4400
        every { sanitizeProductNameInput(any()) } returns "Ticket"

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("44") // input in EUR
        viewModel.buyTicket()

        verify(exactly = 1) { sanitizePriceInput("44") }
        verify(exactly = 1) { sanitizeProductNameInput("Ticket") }

        viewModel.buyTicketState.test {
            val state = awaitItem()
            Truth.assertThat(state).isInstanceOf(BuyTicketState.Error::class.java)
            val err = state as BuyTicketState.Error
            Assert.assertEquals(err.errorMessage, ErrorMessage.ServerError)
        }
    }

    @Test
    fun enterInfo_createTicket_ClientError() = runTest {
        coEvery {
            createTicket(any(), any())
        } returns Result.Error(ApiException(code = 400))

        every { sanitizePriceInput(any()) } returns 4400
        every { sanitizeProductNameInput(any()) } returns "Ticket"

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("44") // input in EUR
        viewModel.buyTicket()

        verify(exactly = 1) { sanitizePriceInput("44") }
        verify(exactly = 1) { sanitizeProductNameInput("Ticket") }

        viewModel.buyTicketState.test {
            val state = awaitItem()
            Truth.assertThat(state).isInstanceOf(BuyTicketState.Error::class.java)
            val err = state as BuyTicketState.Error
            Assert.assertEquals(err.errorMessage, ErrorMessage.ClientError)
        }
    }

    @Test
    fun enterInfo_createTicket_UnknownError() = runTest {
        coEvery {
            createTicket(any(), any())
        } returns Result.Error(Throwable("Something went wrong"))

        every { sanitizePriceInput(any()) } returns 4400
        every { sanitizeProductNameInput(any()) } returns "Ticket"

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("44") // input in EUR
        viewModel.buyTicket()

        verify(exactly = 1) { sanitizePriceInput("44") }
        verify(exactly = 1) { sanitizeProductNameInput("Ticket") }

        viewModel.buyTicketState.test {
            val state = awaitItem()
            Truth.assertThat(state).isInstanceOf(BuyTicketState.Error::class.java)
            val err = state as BuyTicketState.Error
            Assert.assertEquals(err.errorMessage, ErrorMessage.UnknownError)
        }
    }
}