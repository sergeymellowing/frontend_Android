package com.mellowingfactory.sleepology

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telephony.MbmsDownloadSession.RESULT_CANCELLED
import android.telephony.SmsManager.RESULT_CANCELLED
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.core.Amplify
import com.mellowingfactory.sleepology.ui.auth.*
import com.mellowingfactory.sleepology.ui.theme.SleepologyTheme
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel
import com.mellowingfactory.sleepology.viewmodel.StatisticsViewModel
import com.mellowingfactory.sleepology.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    private val statisticsViewModel by viewModels<StatisticsViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            Amplify.Auth.handleWebUISignInResponse(data)
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
                SessionScreen(viewModel = viewModel, statisticsViewModel = statisticsViewModel, userViewModel = userViewModel)
            }
        }
    }
}