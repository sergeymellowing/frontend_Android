package com.mellowingfactory.sleepology.services

import android.content.Context
import android.util.Log
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.google.gson.Gson
import com.mellowingfactory.sleepology.models.*
import com.mellowingfactory.sleepology.static.API_NAME
import com.mellowingfactory.sleepology.static.Fun
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

interface AmplifyService {
    fun configureAmplify(context: Context)
    fun signUp(state: SignUpState, onComplete: () -> Unit)
    fun verifyCode(state: VerificationState, onComplete: () -> Unit)
    fun login(state: LoginState, onComplete: () -> Unit)
    fun loginWithProvider(onComplete: () -> Unit)
    fun logOut(onComplete: () -> Unit)
    fun fetchCurrentAuthSession(onComplete: () -> Unit)

    fun getStatistics(journalDate: Date?, timeFrame: Timeframe, onComplete: () -> Unit)
}

class AmplifyServiceImpl: AmplifyService {
    override fun configureAmplify(context: Context) {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
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

    override fun loginWithProvider(onComplete: () -> Unit) {
        // TODO: pass MainActivity or another one to this function
        // TODO: set callbacks in AdnroidManifest file
        // TODO: setup android auth on providers side
//        Amplify.Auth.signInWithSocialWebUI(
//            AuthProvider.facebook(),
//            activity,
//            { result: AuthSignInResult -> Log.i("AuthQuickstart", result.toString()) },
//            { error: AuthException -> Log.e("AuthQuickstart", error.toString()) }
//        )
    }

    override fun logOut(onComplete: () -> Unit) {
        Amplify.Auth.signOut(
            { onComplete() },
            { Log.e("Sleepology", "LogOut Failed", it) }
        )
    }

    override fun fetchCurrentAuthSession(onComplete: () -> Unit) {
        Amplify.Auth.fetchAuthSession(
            {
                val session = it as AWSCognitoAuthSession
                when (session.identityId.type) {
                    AuthSessionResult.Type.SUCCESS -> {
                        Log.i("AuthQuickStart", "IdentityId = ${session.identityId.value}")
                        // TODO: tokens for recording sound and upload to the server
                        println(session.userPoolTokens.value?.accessToken)
                    }

                    AuthSessionResult.Type.FAILURE ->
                        Log.w("AuthQuickStart", "IdentityId not found", session.identityId.error)
                }
            },
            { Log.e("AuthQuickStart", "Failed to fetch session", it) }
        )
    }

    override fun getStatistics(journalDate: Date?, timeFrame: Timeframe, onComplete: () -> Unit) {
        val timeZoneOffset = Fun.getTimezoneOffset()

        val journalDateString = journalDate.toString()

        val queryParameters = mutableMapOf<String, String>()
        queryParameters["timeZoneOffset"] = timeZoneOffset
        queryParameters["timeFrame"] = timeFrame.toString()
        if (journalDate != null) {
            queryParameters["date"] = journalDateString
        }

        println(queryParameters)

        val request = RestOptions.builder()
            .addPath("/statistics/$timeFrame")
            .addQueryParameters(queryParameters)
            .build()

        Amplify.API.get(API_NAME, request,
            {
                val result = it.data.asJSONObject().getJSONObject("data").toString()
                val gson = Gson()
                val statistics: StatisticsResponse = gson.fromJson(result, StatisticsResponse::class.java)
                Log.i("MyAmplifyApp", "GET statistics succeeded: $statistics")
            },
            { Log.e("MyAmplifyApp", "GET failed.", it) }
        )
    }
}