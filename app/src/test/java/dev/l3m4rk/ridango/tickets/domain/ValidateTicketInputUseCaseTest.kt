package dev.l3m4rk.ridango.tickets.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateTicketInputUseCaseTest {
    private val useCase = ValidateTicketInputUseCase()

    @Test
    fun testEmptyInputReturnsFalse() {
        assertThat(useCase("", "")).isFalse()
    }

    @Test
    fun testBothInputsEnteredReturnsTrue() {
        assertThat(useCase("Product", "12.3")).isTrue()
    }

    @Test
    fun testOneInputMissingReturnsFalse() {
        assertThat(useCase("", "12.3")).isFalse()
        assertThat(useCase("Product", "")).isFalse()
    }
}
