package com.tharsis.quickservices.presentation.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tharsis.quickservices.presentation.views.booking.BookingScreen
import com.tharsis.quickservices.presentation.views.confirmation.ConfirmationScreen
import com.tharsis.quickservices.presentation.views.payment.PaymentErrorFallback
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
        startDestination = Screen.Services.route
    ) {
        composable(
            route = Screen.Services.route,
            enterTransition = { forwardEnterTransition(slideSpec, fadeSpec) },
            exitTransition = { forwardExitTransition(slideSpec, fadeSpec) },
            popEnterTransition = { backEnterTransition(slideSpec, fadeSpec) },
            popExitTransition = { backExitTransition(slideSpec, fadeSpec) }
        ) {
            ServicesScreen(
                onServiceClick = { serviceId ->
                    navController.navigate(Screen.Booking.createRoute(serviceId))
                }
            )
        }

        composable(
            route = Screen.Booking.route,
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
                onBookingCreated =  { bookingId, userEmail ->
                    navController.navigate(Screen.Payment.createRoute(bookingId,userEmail)) {
                        popUpTo(Screen.Booking.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument(Constants.ARG_BOOKING_ID) {
                    type = NavType.StringType
                },
                navArgument(Constants.ARG_USER_EMAIL) {
                    type = NavType.StringType
                }
            ),
            enterTransition = { forwardEnterTransition(slideSpec, fadeSpec) },
            exitTransition = { forwardExitTransition(slideSpec, fadeSpec) },
            popEnterTransition = { backEnterTransition(slideSpec, fadeSpec) },
            popExitTransition = { backExitTransition(slideSpec, fadeSpec) }
        ) {
            val userEmail = it.arguments?.getString(Constants.ARG_USER_EMAIL)
            Log.d("PaymentScreen", "userEmail: $userEmail")

            if (userEmail != null) {
                PaymentScreen(
                    userEmail = userEmail,
                    onNavigateBack = { navController.popBackStack() },
                    onPaymentSuccess = { bookingId ->
                        navController.navigate(Screen.Confirmation.createRoute(bookingId)) {
                            popUpTo(Screen.Services.route) { inclusive = false }
                        }
                    }
                )
            } else {
                PaymentErrorFallback(
                    onNavigateBack = {
                        navController.navigate(Screen.Services.route) {
                            popUpTo(Screen.Services.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(
            route = Screen.Confirmation.route,
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
                    navController.navigate(Screen.Services.route) {
                        popUpTo(Screen.Services.route) {
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

sealed class Screen(val route: String) {
    object Services : Screen("services")

    object Booking : Screen("booking/{${Constants.ARG_SERVICE_ID}}") {
        fun createRoute(serviceId: String) = "booking/$serviceId"
    }

    object Payment : Screen("payment/{${Constants.ARG_BOOKING_ID}}/{${Constants.ARG_USER_EMAIL}}") {
        fun createRoute(bookingId: String, userEmail: String) = "payment/$bookingId/$userEmail"
    }

    object Confirmation : Screen("confirmation/{${Constants.ARG_BOOKING_ID}}") {
        fun createRoute(bookingId: String) = "confirmation/$bookingId"
    }
}