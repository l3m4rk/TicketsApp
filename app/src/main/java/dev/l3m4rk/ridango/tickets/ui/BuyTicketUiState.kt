package dev.l3m4rk.ridango.tickets.ui

data class BuyTicketUiState(
    val productName: String = "",
    val price: String = "",
    val buttonEnabled: Boolean = false,
)

sealed interface BuyTicketState {
    object Init : BuyTicketState
    object InProgress : BuyTicketState
    data class Success(val message: String) : BuyTicketState

    data class Error(val errorMessage: ErrorMessage) : BuyTicketState
}
