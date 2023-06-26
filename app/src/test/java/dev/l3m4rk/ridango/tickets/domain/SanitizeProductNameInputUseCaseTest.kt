package dev.l3m4rk.ridango.tickets.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class SanitizeProductNameInputUseCaseTest {

    private val sanitizeProductNameInput = SanitizeProductNameInputUseCaseImpl()

    @Test
    fun `check sanitize removes spaces from start and end`() {
        assertEquals("test", sanitizeProductNameInput("   test  "))
        assertEquals("test", sanitizeProductNameInput("test"))
    }
}
