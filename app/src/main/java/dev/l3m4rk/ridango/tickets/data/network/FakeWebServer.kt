package dev.l3m4rk.ridango.tickets.data.network

import dev.l3m4rk.ridango.tickets.TicketOuterClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer

interface FakeWebServer {

    fun start(port: Int = 8080)

    // instead of parsing we are passing data directly for simplicity
    fun enqueueTicketResponse(productName: String, price: Int)
}

internal class FakeWebServerImpl(
    dispatcher: CoroutineDispatcher,
) : FakeWebServer {

    private var id: Int = 0
    private lateinit var webServer: MockWebServer

    private val scope = CoroutineScope(dispatcher)

    override fun start(port: Int) {
        scope.launch {
            webServer = MockWebServer()
            webServer.start(port)
        }
    }

    override fun enqueueTicketResponse(productName: String, price: Int) {
        TicketOuterClass.Ticket.newBuilder()
            .setId(++id)
            .setProductName(productName)
            .setPrice(price)
            .build()
            .toByteArray()
            .let { bytes ->
                Buffer().write(bytes)
            }
            .let { buffer ->
                MockResponse().apply {
                    setBody(buffer)
                }
            }.also { mockResponse ->
                webServer.enqueue(mockResponse)
            }
    }
}
