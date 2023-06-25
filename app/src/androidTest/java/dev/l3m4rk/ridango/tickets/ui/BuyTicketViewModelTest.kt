package dev.l3m4rk.ridango.tickets.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.l3m4rk.ridango.tickets.TicketOuterClass.Ticket
import dev.l3m4rk.ridango.tickets.domain.CreateTicketUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizePriceInputUseCase
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class BuyTicketViewModelTest {

    private val validateTicketInput = ValidateTicketInputUseCase()
    private val sanitizePriceInput = mockk<SanitizePriceInputUseCase>()
    private val createTicket = mockk<CreateTicketUseCase>()

    private val testScheduler = TestCoroutineScheduler()

    private val priceInCents = 2300
    private val successTicketResponse = Ticket.newBuilder()
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

        assertThat(uiState.buttonEnabled).isFalse()
        assertThat(uiState.productName).isEmpty()
        assertThat(uiState.price).isEmpty()

        assertThat(viewModel.buyTicketState.value).isEqualTo(BuyTicketState.Init)
    }

    @Test
    fun productInfoFilled_buttonEnabled() = runTest {
        assertFalse(viewModel.uiState.value.buttonEnabled)

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("32") // input in EUR

        viewModel.uiState.test {
            assertTrue(awaitItem().buttonEnabled)
        }
    }

    @Test
    fun enterInfo_SuccessfulTicketCreated() = runTest {

        coEvery {
            createTicket(any(), any())
        } returns Result.Success(successTicketResponse)

        every { sanitizePriceInput(any()) } returns priceInCents

        viewModel.changeProductName("Ticket")
        viewModel.changePrice("23") // input in EUR
        viewModel.buyTicket()

        verify(exactly = 1) { sanitizePriceInput("23") }

        viewModel.buyTicketState.test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(BuyTicketState.Success::class.java)
            assertEquals(
                (state as BuyTicketState.Success).message,
                "Ticket id=${successTicketResponse.id} name=${successTicketResponse.productName} price=${successTicketResponse.price} created"
            )
        }
    }
}