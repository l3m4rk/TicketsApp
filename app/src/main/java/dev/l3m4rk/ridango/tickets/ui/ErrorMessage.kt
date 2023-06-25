package dev.l3m4rk.ridango.tickets.ui

import android.content.Context
import dev.l3m4rk.ridango.tickets.R

enum class ErrorMessage {
    NetworkError,
    ServerError,
    ClientError,
    UnknownError,
}

fun ErrorMessage.asString(context: Context): String {
    return when (this) {
        ErrorMessage.NetworkError -> context.getString(R.string.error_network)
        ErrorMessage.ServerError -> context.getString(R.string.error_server)
        ErrorMessage.ClientError -> context.getString(R.string.error_client)
        ErrorMessage.UnknownError -> context.getString(R.string.error_unknown)
    }
}
