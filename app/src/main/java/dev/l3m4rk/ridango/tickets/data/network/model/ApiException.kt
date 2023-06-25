package dev.l3m4rk.ridango.tickets.data.network.model

class ApiException(val code: Int) : Exception() {
    val isClient: Boolean
        get() = code in 400..499

    val isServer: Boolean
        get() = code in 500..599

    override fun toString(): String {
        val cause = when {
            isClient -> "client error"
            isServer -> "server error"
            else -> ""
        }
        return "ApiException(code=$code, $cause)"
    }
}
