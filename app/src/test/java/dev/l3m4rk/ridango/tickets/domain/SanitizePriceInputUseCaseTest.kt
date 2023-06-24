package dev.l3m4rk.ridango.tickets.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SanitizePriceInputUseCaseTest {

    private val sanitizePriceInput = SanitizePriceInputUseCaseImpl()

    @Test
    fun `correct price converts to cents`() {
        val input = "23.12"
        val expectedOutputInCents = 2312
        assertThat(sanitizePriceInput(input)).isEqualTo(expectedOutputInCents)
    }

    @Test
    fun `incorrect price converts to zero cents`() {
        val input = listOf("wqerewrqer", "").random()
        val expectedOutputInCents = 0
        assertThat(sanitizePriceInput(input)).isEqualTo(expectedOutputInCents)
    }
}
