package com.tharsis.quickservices.presentation.views.services

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tharsis.quickservices.presentation.components.EmptyContent
import com.tharsis.quickservices.presentation.components.ErrorContent
import com.tharsis.quickservices.presentation.components.ServicesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    onServiceClick: (String) -> Unit,
    viewModel: ServicesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickServe") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.hasError() -> {
                    ErrorContent(
                        message = state.errorMessage ?: "Unknown error",
                        onRetry = { viewModel.retry() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.isEmpty() -> {
                    EmptyContent(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    ServicesList(
                        services = state.getFilteredServices(),
                        onServiceClick = onServiceClick
                    )
                }
            }
        }
    }
}
