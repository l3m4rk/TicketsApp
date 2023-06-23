package dev.l3m4rk.ridango.tickets.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.l3m4rk.ridango.tickets.R

@Composable
fun BuyTicketScreen(viewModel: BuyTicketViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState() // TODO: collectAsStateWithLifecycle
    BuyTicketScreen(
        state,
        onChangeProductName = viewModel::changeProductName,
        onChangePrice = viewModel::changePrice,
        onBuyTicket = viewModel::onBuyTicket,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketScreen(
    state: BuyProductUiState,
    onChangeProductName: (productNameValue: String) -> Unit = {},
    onChangePrice: (priceValue: String) -> Unit = {},
    onBuyTicket: () -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.productName,
            onValueChange = onChangeProductName,
            singleLine = true,
            label = { Text(stringResource(R.string.label_product_name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.price,
            onValueChange = onChangePrice,
            singleLine = true,
            label = { Text(stringResource(R.string.label_price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = state.buttonEnabled,
            onClick = onBuyTicket,
        ) {
            Text(text = stringResource(R.string.button_buy_ticket))
        }
    }
}

@Preview
@Composable
fun BuyTicketScreenPreview() {
    Surface {
        BuyTicketScreen()
    }
}
