package dev.l3m4rk.ridango.tickets.presentation

data class BuyTicketUiState(
    val productName: String = "",
    val price: String = "",
    val buttonEnabled: Boolean = false,
)
