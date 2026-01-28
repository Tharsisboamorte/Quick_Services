package com.tharsis.quickservices.presentation.views.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tharsis.quickservices.R
import com.tharsis.quickservices.utils.DateTimeUtil


@Composable
fun ConfirmationScreen(
    onNavigateHome: () -> Unit,
    viewModel: ConfirmationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.confirmation_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        state.booking?.let { booking ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        stringResource(
                            R.string.confirmation_service,
                            booking.serviceName
                        )
                    )
                    Text(
                        stringResource(
                            R.string.confirmation_date,
                            DateTimeUtil.formatDate(booking.scheduledTimestamp)
                        )
                    )
                    Text(
                        stringResource(
                            R.string.confirmation_time,
                            DateTimeUtil.formatTime(booking.scheduledTimestamp)
                        )
                    )
                    Text(
                        stringResource(
                            R.string.confirmation_amount,
                            booking.servicePrice
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.calendarEventId == null) {
            OutlinedButton(
                onClick = { viewModel.addToCalendar() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isAddingToCalendar
            ) {
                Text(stringResource(R.string.confirmation_add_calendar))
            }
        } else {
            Text(
                stringResource(R.string.confirmation_added_calendar),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.confirmation_done))
        }
    }
}