package dev.l3m4rk.ridango.tickets.ui.buyTicket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.l3m4rk.ridango.tickets.data.network.model.ApiException
import dev.l3m4rk.ridango.tickets.domain.CreateTicketUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizePriceInputUseCase
import dev.l3m4rk.ridango.tickets.domain.SanitizeProductNameInputUseCase
import dev.l3m4rk.ridango.tickets.domain.ValidateTicketInputUseCase
import dev.l3m4rk.ridango.tickets.util.WhenUiSubscribed
import dev.l3m4rk.ridango.tickets.util.core.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BuyTicketViewModel @Inject constructor(
    private val validateTicketInput: ValidateTicketInputUseCase,
    private val sanitizePriceInput: SanitizePriceInputUseCase,
    private val sanitizeProductNameInput: SanitizeProductNameInputUseCase,
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

    fun buyTicket() {
        val price: Int = sanitizePriceInput(this.price.value)
        val productName: String = sanitizeProductNameInput(this.productName.value)

        _buyTicketState.value = BuyTicketState.InProgress

        viewModelScope.launch {
            when (val result = createTicket(productName, price)) {
                is Result.Success -> {
                    val ticket = result.data
                    val message =
                        "Ticket id=${ticket.id} name=${ticket.productName} " +
                            "price=${ticket.price} created"
                    Timber.i("Success $message")
                    _buyTicketState.value = BuyTicketState.Success(message)
                }
                is Result.Error -> {
                    val errorMessage = result.t.toErrorMessage()
                    Timber.e(errorMessage.toString())
                    _buyTicketState.value = BuyTicketState.Error(errorMessage)
                }
            }
        }
    }

    private fun Throwable.toErrorMessage(): ErrorMessage {
        return when {
            this is IOException -> ErrorMessage.NetworkError
            this is ApiException && this.isServer -> ErrorMessage.ServerError
            this is ApiException && this.isClient -> ErrorMessage.ClientError
            else -> ErrorMessage.UnknownError
        }
    }
}

