package com.tharsis.quickservices.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tharsis.quickservices.presentation.views.booking.BookingScreen
import com.tharsis.quickservices.presentation.views.confirmation.ConfirmationScreen
import com.tharsis.quickservices.presentation.views.payment.PaymentScreen
import com.tharsis.quickservices.presentation.views.services.ServicesScreen
import com.tharsis.quickservices.utils.Constants


@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Constants.ROUTE_SERVICES
    ) {
        composable(route = Constants.ROUTE_SERVICES) {
            ServicesScreen(
                onServiceClick = { serviceId ->
                    navController.navigate("booking/$serviceId")
                }
            )
        }

        composable(
            route = Constants.ROUTE_BOOKING,
            arguments = listOf(
                navArgument(Constants.ARG_SERVICE_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            BookingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onBookingCreated = { bookingId ->
                    navController.navigate("payment/$bookingId") {
                        popUpTo("booking/{serviceId}") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Constants.ROUTE_PAYMENT,
            arguments = listOf(
                navArgument(Constants.ARG_BOOKING_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            PaymentScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPaymentSuccess = { bookingId ->
                    navController.navigate("confirmation/$bookingId") {
                        popUpTo(Constants.ROUTE_SERVICES) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(
            route = Constants.ROUTE_CONFIRMATION,
            arguments = listOf(
                navArgument(Constants.ARG_BOOKING_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            ConfirmationScreen(
                onNavigateHome = {
                    navController.navigate(Constants.ROUTE_SERVICES) {
                        popUpTo(Constants.ROUTE_SERVICES) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

object NavigationRoutes {

    fun bookingRoute(serviceId: String): String {
        return "booking/$serviceId"
    }

    fun paymentRoute(bookingId: String): String {
        return "payment/$bookingId"
    }

    fun confirmationRoute(bookingId: String): String {
        return "confirmation/$bookingId"
    }
}