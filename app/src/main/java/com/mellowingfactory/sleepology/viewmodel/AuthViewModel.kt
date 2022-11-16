package com.mellowingfactory.sleepology.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mellowingfactory.sleepology.models.*
import com.mellowingfactory.sleepology.services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val apiNodeServer: ApiNodeServer = ApiNodeServer()

    lateinit var navigateTo: (String) -> Unit

    var loginState = mutableStateOf(LoginState())
        private set

    var signUpState = mutableStateOf(SignUpState())
        private set

    var verificationState = mutableStateOf(VerificationState())
        private set

    var username = mutableStateOf("")
        private set

    fun updateSignUpState(name: String? = null,
                          familyName: String? = null,
                          marketing: Boolean? = true,
                          email: String? = null,
                          password: String? = null) {
        email?.let {
            signUpState.value = signUpState.value.copy(email = it)
            verificationState.value = verificationState.value.copy(username = it)
        }
        name?.let { signUpState.value = signUpState.value.copy(name = it) }
        familyName?.let { signUpState.value = signUpState.value.copy(familyName = it) }
        marketing?.let { signUpState.value = signUpState.value.copy(marketing = it) }
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
        apiNodeServer.configureAmplify(context)
    }

    fun showSignUp() {
        navigateTo("signUp")
    }

    fun showLogin() {
                navigateTo("login")
    }

    fun signUp() {
        apiNodeServer.signUp(signUpState.value) {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("verify")
            }
        }
    }

    fun verifyCode() {
        apiNodeServer.verifyCode(verificationState.value) {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("login")
            }
        }
    }

    fun login() {
        apiNodeServer.login(loginState.value) {
            viewModelScope.launch(Dispatchers.Main) {
                if (it) {
                    navigateTo("session")
                } else {
                    navigateTo("login")
                }

            }
        }
    }

    fun logOut() {
        apiNodeServer.logOut {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("login")
            }
        }
    }

    fun loginWithProvider() {
        apiNodeServer.loginWithProvider() {
            viewModelScope.launch(Dispatchers.Main) {
                navigateTo("session")
            }
        }
    }

    fun getCurrentAuthSession(onComplete: (Boolean) -> Unit) {
        apiNodeServer.fetchCurrentAuthSession { name, session ->
            name?.let { username.value = name }
            viewModelScope.launch(Dispatchers.Main) {
                if (session.isSignedIn) {
                    navigateTo("session")
                    onComplete(true)
                } else {
                    navigateTo("login")
                    onComplete(false)
                }

            }
        }
    }
}