package com.mellowingfactory.sleepology

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mellowingfactory.sleepology.ui.auth.*
import com.mellowingfactory.sleepology.ui.theme.SleepologyTheme
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.configureAmplify(this)
        viewModel.getCurrentAuthSession()

        setContent {
            SleepologyTheme {
                AppNavigator()
            }
        }
    }

    @Composable
    fun AppNavigator() {
        val navController = rememberNavController()
        viewModel.navigateTo = {
            navController.navigate(it)
        }

        NavHost(navController = navController, startDestination = "session") {
            composable("login") {
                LoginScreen(viewModel = viewModel)
            }

            composable("signUp") {
                SignUpScreen(viewModel = viewModel)
            }

            composable("verify") {
                ConfirmationScreen(viewModel = viewModel)
            }

            composable("session") {
                SessionScreen(viewModel = viewModel)
            }
        }
    }
}