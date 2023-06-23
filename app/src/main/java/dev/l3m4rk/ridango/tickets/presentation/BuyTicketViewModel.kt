package dev.l3m4rk.ridango.tickets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.l3m4rk.ridango.tickets.domain.ValidateTicketInputUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BuyTicketViewModel @Inject constructor(
    private val validateTicketInput: ValidateTicketInputUseCase
) : ViewModel() {

    private val productName = MutableStateFlow("")
    private val price = MutableStateFlow("")

    val uiState = combine(productName, price) { productName, price ->
        BuyProductUiState(
            productName = productName,
            price = price,
            buttonEnabled = validateTicketInput(productName, price)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = BuyProductUiState()
    )

    fun changeProductName(productNameValue: String) {
        productName.value = productNameValue
    }

    fun changePrice(priceValue: String) {
        price.value = priceValue
    }

    fun onBuyTicket() {
        // TODO: sanitize product name
        // TODO: handle ticket buying
    }
}
