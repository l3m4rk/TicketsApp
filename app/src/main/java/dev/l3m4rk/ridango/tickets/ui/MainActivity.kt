package dev.l3m4rk.ridango.tickets.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.l3m4rk.ridango.tickets.ui.theme.TicketsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketsTheme {
                TicketsApp(Modifier.fillMaxSize())
            }
        }
    }
}
