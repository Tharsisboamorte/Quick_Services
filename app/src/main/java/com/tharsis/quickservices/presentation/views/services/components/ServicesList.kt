package com.tharsis.quickservices.presentation.views.services.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.tharsis.quickservices.domain.model.Service


@Composable
fun ServicesList(
    services: List<Service>,
    onServiceClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = services,
            key = { service -> service.id }
        ) { service ->
            ServiceCard(
                service = service,
                onClick = { onServiceClick(service.id) }
            )
        }
    }
}