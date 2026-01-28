package com.tharsis.quickservices.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
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
    val slideSpec = tween<IntOffset>(
        durationMillis = 320,
        easing = FastOutSlowInEasing
    )
    val fadeSpec = tween<Float>(durationMillis = 120)

    NavHost(
        navController = navController,
        startDestination = Constants.ROUTE_SERVICES
    ) {
        composable(
            route = Constants.ROUTE_SERVICES,
            enterTransition = { forwardEnterTransition(slideSpec, fadeSpec) },
            exitTransition = { forwardExitTransition(slideSpec, fadeSpec) },
            popEnterTransition = { backEnterTransition(slideSpec, fadeSpec) },
            popExitTransition = { backExitTransition(slideSpec, fadeSpec) }
        ) {
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
            ),
            enterTransition = { forwardEnterTransition(slideSpec, fadeSpec) },
            exitTransition = { forwardExitTransition(slideSpec, fadeSpec) },
            popEnterTransition = { backEnterTransition(slideSpec, fadeSpec) },
            popExitTransition = { backExitTransition(slideSpec, fadeSpec) }
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
            ),
            enterTransition = { forwardEnterTransition(slideSpec, fadeSpec) },
            exitTransition = { forwardExitTransition(slideSpec, fadeSpec) },
            popEnterTransition = { backEnterTransition(slideSpec, fadeSpec) },
            popExitTransition = { backExitTransition(slideSpec, fadeSpec) }
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
            ),
            enterTransition = { forwardEnterTransition(slideSpec, fadeSpec) },
            exitTransition = { forwardExitTransition(slideSpec, fadeSpec) },
            popEnterTransition = { backEnterTransition(slideSpec, fadeSpec) },
            popExitTransition = { backExitTransition(slideSpec, fadeSpec) }
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

private fun forwardEnterTransition(
    slideSpec: FiniteAnimationSpec<IntOffset>,
    fadeSpec: FiniteAnimationSpec<Float>
): EnterTransition {
    return slideInHorizontally(
        animationSpec = slideSpec,
        initialOffsetX = { fullWidth -> fullWidth }
    ) + fadeIn(animationSpec = fadeSpec)
}

private fun forwardExitTransition(
    slideSpec: FiniteAnimationSpec<IntOffset>,
    fadeSpec: FiniteAnimationSpec<Float>
): ExitTransition {
    return slideOutHorizontally(
        animationSpec = slideSpec,
        targetOffsetX = { fullWidth -> -fullWidth }
    ) + fadeOut(animationSpec = fadeSpec)
}

private fun backEnterTransition(
    slideSpec: FiniteAnimationSpec<IntOffset>,
    fadeSpec: FiniteAnimationSpec<Float>
): EnterTransition {
    return slideInHorizontally(
        animationSpec = slideSpec,
        initialOffsetX = { fullWidth -> -fullWidth }
    ) + fadeIn(animationSpec = fadeSpec)
}

private fun backExitTransition(
    slideSpec: FiniteAnimationSpec<IntOffset>,
    fadeSpec: FiniteAnimationSpec<Float>
): ExitTransition {
    return slideOutHorizontally(
        animationSpec = slideSpec,
        targetOffsetX = { fullWidth -> fullWidth }
    ) + fadeOut(animationSpec = fadeSpec)
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