package com.mellowingfactory.sleepology.services

import android.content.Context
import android.util.Log
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.mellowingfactory.sleepology.models.*

interface AmplifyService {
    fun configureAmplify(context: Context)
    fun signUp(state: SignUpState, onComplete: () -> Unit)
    fun verifyCode(state: VerificationState, onComplete: () -> Unit)
    fun login(state: LoginState, onComplete: () -> Unit)
    fun logOut(onComplete: () -> Unit)
    fun fetchCurrentAuthSession(onComplete: () -> Unit)
}

class AmplifyServiceImpl: AmplifyService {
    override fun configureAmplify(context: Context) {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
//            Amplify.addPlugin(AWSAPIPlugin())
            Amplify.configure(context)
            Log.i("Sleepology", "Configured amplify")
        } catch (e: Exception) {
            Log.i("Sleepology", "Amplify configuration failed", e)
        }
    }

    override fun signUp(state: SignUpState, onComplete: () -> Unit) {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), state.email)
            .build()

        Amplify.Auth.signUp(state.username, state.password, options,
            { onComplete() },
            { Log.e("Sleepology", "Sign Up Error:", it) }
        )
    }

    override fun verifyCode(state: VerificationState, onComplete: () -> Unit) {
        Amplify.Auth.confirmSignUp(
            state.username,
            state.code,
            { onComplete() },
            { Log.e("Sleepology", "Verification Failed", it) }
        )
    }

    override fun login(state: LoginState, onComplete: () -> Unit) {
        Amplify.Auth.signIn(
            state.username,
            state.password,
            { onComplete() },
            { Log.e("Sleepology", "LogIn Failed", it) }
        )
    }

    override fun logOut(onComplete: () -> Unit) {
        Amplify.Auth.signOut(
            { onComplete() },
            { Log.e("Sleepology", "LogOut Failed", it) }
        )
    }

    override fun fetchCurrentAuthSession(onComplete: () -> Unit) {
        Amplify.Auth.fetchAuthSession(
            { Log.i("Sleepology", "Is signed in: ${it.isSignedIn}") },
            { Log.e("Sleepology", "Failed to fetch session", it) }
        )
    }

}