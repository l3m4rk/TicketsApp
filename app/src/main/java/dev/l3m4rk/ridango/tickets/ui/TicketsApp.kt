package dev.l3m4rk.ridango.tickets.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.l3m4rk.ridango.tickets.ui.TicketsAppDestinations.BuyTicket

@Composable
fun TicketsApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Surface(
        modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BuyTicket
        ) {
            composable(BuyTicket) {
                BuyTicketScreen()
            }
        }
    }
}

object TicketsAppDestinations {
    const val BuyTicket = "buyTicket"
}
