package dev.l3m4rk.ridango.tickets.util

import kotlinx.coroutines.flow.SharingStarted

private const val TIMEOUT_MILLIS = 3_000L

val WhenUiSubscribed = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS)