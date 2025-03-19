package org.savics.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(navController: NavHostController, context: Context) {
    val sharedPreferences = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)
    val email = sharedPreferences.getString("email", "")

    val startDestination = if (email.isNullOrEmpty()) {
        Onboarding.route
    } else {
        Home.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Onboarding.route) {
            Onboarding(navController)
        }

        composable(Home.route) {
            Home(navController)
        }

        composable(Profile.route) {
            Profile(navController)
        }
    }
}