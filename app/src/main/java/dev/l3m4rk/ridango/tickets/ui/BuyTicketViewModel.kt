package dev.l3m4rk.ridango.tickets.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.l3m4rk.ridango.tickets.TicketOuterClass
import dev.l3m4rk.ridango.tickets.domain.CreateTicketUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizePriceInputUseCase
import dev.l3m4rk.ridango.tickets.domain.ValidateTicketInputUseCase
import dev.l3m4rk.ridango.tickets.util.WhenUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BuyTicketViewModel @Inject constructor(
    private val validateTicketInput: ValidateTicketInputUseCase,
    private val sanitizePriceInput: SanitizePriceInputUseCase,
    private val createTicket: CreateTicketUseCase,
) : ViewModel() {

    private val productName = MutableStateFlow("")
    private val price = MutableStateFlow("")

    private val _buyTicketState = MutableStateFlow<BuyTicketState>(BuyTicketState.Init)
    val buyTicketState = _buyTicketState.asStateFlow()

    private val _uiState = MutableStateFlow(BuyTicketUiState())
    val uiState = combine(productName, price, _uiState) { productName, price, state ->
        state.copy(
            productName = productName,
            price = price,
            buttonEnabled = validateTicketInput(productName, price),
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhenUiSubscribed,
        initialValue = BuyTicketUiState()
    )

    fun changeProductName(productNameValue: String) {
        productName.value = productNameValue
    }

    fun changePrice(priceValue: String) {
        price.value = priceValue
    }

    fun onBuyTicket() {
        val price: Int = sanitizePriceInput(this.price.value)

        _buyTicketState.value = BuyTicketState.InProgress

        viewModelScope.launch {

            val result = createTicket(productName.value, price)

            if (result.isSuccess) {
                result.getOrNull()?.also { ticket: TicketOuterClass.Ticket ->
                    val message = "Ticket id=${ticket.id} " +
                        "product=${ticket.productName} price=${ticket.price} created"
                    Timber.i("Success $message")
                    _buyTicketState.value = BuyTicketState.Success(message)
                }
            } else {
                result.exceptionOrNull()?.also { t ->
                    val errorMessage = t.message
                    Timber.e(errorMessage)
                    _buyTicketState.value = BuyTicketState.Error(errorMessage ?: "Something went wrong")
                }
            }
        }
    }
}
