package com.tharsis.quickservices.presentation.views.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tharsis.quickservices.domain.model.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onNavigateBack: () -> Unit,
    onPaymentSuccess: (String) -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onPaymentSuccess(state.bookingId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Amount", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "R$ %.2f".format(state.amount),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }

            Text("Payment Method", style = MaterialTheme.typography.titleMedium)

            PaymentMethod.entries.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = state.selectedPaymentMethod == method,
                            onClick = { viewModel.selectPaymentMethod(method) }
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.selectedPaymentMethod == method,
                        onClick = { viewModel.selectPaymentMethod(method) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(method.displayName)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isProcessing
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Pay Now")
                }
            }
        }
    }
}