package dev.l3m4rk.ridango.tickets.domain

import javax.inject.Inject

interface SanitizeProductNameInputUseCase {
    operator fun invoke(productNameInput: String): String
}

class SanitizeProductNameInputUseCaseImpl @Inject constructor() : SanitizeProductNameInputUseCase {
    override fun invoke(productNameInput: String): String {
        return productNameInput.trim()
    }
}
