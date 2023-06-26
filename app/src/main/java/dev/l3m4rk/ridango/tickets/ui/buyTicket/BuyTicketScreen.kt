package dev.l3m4rk.ridango.tickets.ui.buyTicket

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.l3m4rk.ridango.tickets.R

@Composable
fun BuyTicketScreen(viewModel: BuyTicketViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val buyTicketState by viewModel.buyTicketState.collectAsStateWithLifecycle()
    BuyTicketScreen(
        state = state,
        buyTicketState = buyTicketState,
        onChangeProductName = viewModel::changeProductName,
        onChangePrice = viewModel::changePrice,
        onBuyTicket = viewModel::buyTicket,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketScreen(
    state: BuyTicketUiState,
    buyTicketState: BuyTicketState,
    onChangeProductName: (productNameValue: String) -> Unit = {},
    onChangePrice: (priceValue: String) -> Unit = {},
    onBuyTicket: () -> Unit = {},
) {

    val context = LocalContext.current

    when (buyTicketState) {
        is BuyTicketState.Error -> {
            LaunchedEffect(buyTicketState) {
                val message = buyTicketState.errorMessage.asString(context)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        BuyTicketState.InProgress -> {
            BuyTicketProgress()
        }

        BuyTicketState.Init -> {

        }

        is BuyTicketState.Success -> {
            LaunchedEffect(buyTicketState) {
                Toast.makeText(context, buyTicketState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProductNameInput(state.productName, onChangeProductName)
        Spacer(Modifier.height(8.dp))
        PriceInput(state.price, onChangePrice)
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = state.buttonEnabled,
            onClick = onBuyTicket,
        ) {
            Text(text = stringResource(R.string.button_buy_ticket))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PriceInput(price: String, onChangePrice: (priceValue: String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = price,
        onValueChange = onChangePrice,
        singleLine = true,
        label = { Text(stringResource(R.string.label_price)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProductNameInput(
    productName: String,
    onChangeProductName: (productNameValue: String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = productName,
        onValueChange = onChangeProductName,
        singleLine = true,
        label = { Text(stringResource(R.string.label_product_name)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun BuyTicketProgress() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun BuyTicketScreenPreview() {
    Surface {
        BuyTicketScreen()
    }
}
