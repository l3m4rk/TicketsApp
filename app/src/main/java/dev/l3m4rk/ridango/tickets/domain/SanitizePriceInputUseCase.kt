package dev.l3m4rk.ridango.tickets.domain

import javax.inject.Inject

/**
 * Sanitizes price input and returns the integer price adjusted to cents
 */
interface SanitizePriceInputUseCase {
    operator fun invoke(priceInput: String): Int
}

class SanitizePriceInputUseCaseImpl @Inject constructor() : SanitizePriceInputUseCase {
    override fun invoke(priceInput: String): Int {
        return ((priceInput.toFloatOrNull() ?: 0f) * CENTS_IN_EURO).toInt()
    }

    companion object {
        private const val CENTS_IN_EURO = 100
    }
}
