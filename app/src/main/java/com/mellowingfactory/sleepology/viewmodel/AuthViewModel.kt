package com.mellowingfactory.sleepology.viewmodel

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mellowingfactory.sleepology.models.*
import com.mellowingfactory.sleepology.services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val amplifyService: AmplifyService = AmplifyServiceImpl()

    lateinit var navigateTo: (String) -> Unit

    var loginState = mutableStateOf(LoginState())
        private set

    var signUpState = mutableStateOf(SignUpState())
        private set

    var verificationState = mutableStateOf(VerificationState())
        private set

    fun updateSignUpState(username: String? = null, email: String? = null, password: String? = null) {
        username?.let {
            signUpState.value = signUpState.value.copy(username = it)
            verificationState.value = verificationState.value.copy(username = it)
        }
        email?.let { signUpState.value = signUpState.value.copy(email = it) }
        password?.let { signUpState.value = signUpState.value.copy(password = it) }
    }

    fun updateLoginState(username: String? = null, password: String? = null) {
        username?.let { loginState.value = loginState.value.copy(username = it) }
        password?.let { loginState.value = loginState.value.copy(password = it) }
    }

    fun updateVerificationCodeState(code: String) {
        verificationState.value = verificationState.value.copy(code = code)
    }

    fun configureAmplify(context: Context) {
        amplifyService.configureAmplify(context)
    }

    fun showSignUp() {
        navigateTo("signUp")
    }

    fun showLogin() {
                navigateTo("login")
    }

    fun signUp() {
        amplifyService.signUp(signUpState.value) {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("verify")
            }
        }
    }

    fun verifyCode() {
        amplifyService.verifyCode(verificationState.value) {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("login")
            }
        }
    }

    fun login() {
        amplifyService.login(loginState.value) {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("session")
            }
        }
    }

    fun logOut() {
        amplifyService.logOut {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("login")
            }
        }
    }

    fun loginWithProvider() {
        amplifyService.loginWithProvider() {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("session")
            }
        }
    }

    fun getCurrentAuthSession() {
        amplifyService.fetchCurrentAuthSession {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("session")
            }
        }
    }
}