package dev.l3m4rk.ridango.tickets.domain

import javax.inject.Inject

class ValidateTicketInputUseCase @Inject constructor() {
    operator fun invoke(productName: String, price: String): Boolean {
        return productName.isNotEmpty() && price.isNotEmpty()
    }
}
