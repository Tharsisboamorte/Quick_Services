package com.tharsis.quickservices.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateTimeSelection(
    selectedDate: Long?,
    selectedTime: Long?,
    onDateSelected: (Long) -> Unit,
    onTimeSelected: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = { /* Show date picker */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.DateRange, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(selectedDate?.let { "Date selected" } ?: "Select Date")
        }

        OutlinedButton(
            onClick = { /* Show time picker */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Schedule, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(selectedTime?.let { "Time selected" } ?: "Select Time")
        }
    }
}