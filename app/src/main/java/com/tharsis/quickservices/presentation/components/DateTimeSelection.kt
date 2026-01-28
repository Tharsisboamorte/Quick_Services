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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tharsis.quickservices.R
import com.tharsis.quickservices.utils.DateTimeUtil.createTimeFromTimeStamp
import com.tharsis.quickservices.utils.DateTimeUtil.getHourFromTimestamp
import com.tharsis.quickservices.utils.DateTimeUtil.getMinuteFromTimestamp

@Composable
fun DateTimeSelection(
    selectedDate: Long?,
    selectedTime: Long?,
    onDateSelected: (Long) -> Unit,
    onTimeSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.DateRange, null)
            Spacer(modifier = Modifier.width(8.dp))
            val dateLabel = selectedDate?.let { stringResource(R.string.date_selected) }
                ?: stringResource(R.string.select_date)
            Text(dateLabel)
        }

        OutlinedButton(
            onClick = { showTimePicker = true },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Schedule, null)
            Spacer(modifier = Modifier.width(8.dp))
            val timeLabel = selectedTime?.let { stringResource(R.string.time_selected) }
                ?: stringResource(R.string.select_time)
            Text(timeLabel)
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { timestamp ->
                onDateSelected(timestamp)
            },
            onDismiss = { showDatePicker = false },
            initialDate = selectedDate
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { hour, minute ->
                val timestamp = createTimeFromTimeStamp(hour, minute)
                onTimeSelected(timestamp)
            },
            onDismiss = { showTimePicker = false },
            initialHour = selectedTime?.let { getHourFromTimestamp(it) } ?: 9,
            initialMinute = selectedTime?.let { getMinuteFromTimestamp(it) } ?: 0
        )
    }
}