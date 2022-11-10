package com.mellowingfactory.sleepology.services

import android.content.Context
import android.util.Log
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUserAttribute
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
import com.mellowingfactory.sleepology.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

//interface ApiNodeServer {
//    fun configureAmplify(context: Context)
//    fun signUp(state: SignUpState, onComplete: () -> Unit)
//    fun verifyCode(state: VerificationState, onComplete: () -> Unit)
//    fun login(state: LoginState, onComplete: () -> Unit)
//    fun loginWithProvider(onComplete: () -> Unit)
//    fun logOut(onComplete: () -> Unit)
//    fun fetchCurrentAuthSession(onComplete: () -> Unit)
//
//    fun getStatistics(journalDate: Date?, timeFrame: Timeframe, onComplete: () -> Unit)
//}

class ApiNodeServer {
    fun configureAmplify(context: Context) {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(context)
            Log.i("Sleepology", "Configured amplify")
        } catch (e: Exception) {
            Log.i("Sleepology", "Amplify configuration failed", e)
        }
    }

    fun signUp(state: SignUpState, onComplete: () -> Unit) {
        var marketingString = "1"
        if (!state.marketing) {
            marketingString = "0"
        }

        val emailAttribute = AuthUserAttribute(AuthUserAttributeKey.email(), state.email)
        val nameAttribute = AuthUserAttribute(AuthUserAttributeKey.name(), state.name)
        val familyNameAttribute =
            AuthUserAttribute(AuthUserAttributeKey.familyName(), state.familyName)
//        val marketingNameAttribute = AuthUserAttribute(AuthUserAttributeKey.custom("marketing"), marketingString)
        val authUserAttribute: List<AuthUserAttribute> =
            listOf(emailAttribute, nameAttribute, familyNameAttribute)

        val options = AuthSignUpOptions.builder()
            .userAttributes(authUserAttribute)
            .build()

        println(options)
        Amplify.Auth.signUp(state.email, state.password, options,
            { onComplete() },
            { Log.e("Sleepology", "Sign Up Error:", it) }
        )
    }

    fun verifyCode(state: VerificationState, onComplete: () -> Unit) {
        Amplify.Auth.confirmSignUp(
            state.username,
            state.code,
            { onComplete() },
            { Log.e("Sleepology", "Verification Failed", it) }
        )
    }

    fun login(state: LoginState, onComplete: (Boolean) -> Unit) {
        Amplify.Auth.signIn(
            state.username,
            state.password,
            {
                if (it.isSignInComplete) {
                    syncAuthAndApiUser(state.username) {
                        onComplete(true)
                    }
                }
            },
            {
                Log.e("Sleepology", "LogIn Failed", it)
                onComplete(false)
            }
        )
    }

    fun loginWithProvider(onComplete: () -> Unit) {
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

    fun logOut(onComplete: () -> Unit) {
        Amplify.Auth.signOut(
            { onComplete() },
            { Log.e("Sleepology", "LogOut Failed", it) }
        )
    }

    fun fetchCurrentAuthSession(onComplete: (String?, AuthSession) -> Unit) {
        Amplify.Auth.fetchAuthSession(
            {
                val session = it as AWSCognitoAuthSession
                when (session.identityId.type) {
                    AuthSessionResult.Type.SUCCESS -> {
                        Log.i("AuthQuickStart", "IdentityId = ${session.identityId.value}")
                        // TODO: tokens for recording sound and upload to the server
                        println(session.userPoolTokens.value?.accessToken)
                        val username = Amplify.Auth.currentUser.username
                        onComplete(username, session)
                    }

                    AuthSessionResult.Type.FAILURE -> {
                        Log.w("AuthQuickStart", "IdentityId not found", session.identityId.error)
                        onComplete(null, session)
                    }
                }
            },
            { Log.e("AuthQuickStart", "Failed to fetch session", it) }
        )
    }

    fun getStatistics(
        journalDate: Date?,
        timeFrame: Timeframe,
        onComplete: (StatisticsResponse?) -> Unit
    ) {
        val timeZoneOffset = Fun.getTimezoneOffset()

        val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(journalDate)
        var journalDateString: String? = null
        if (journalDate != null) {
            journalDateString = formattedDate.toString()
        }


        val queryParameters = mapOf(
            "timeZoneOffset" to timeZoneOffset,
            "timeFrame" to timeFrame.toString(),
            "date" to journalDateString
        )

        println("queryParameters: $queryParameters")

        val request = RestOptions.builder()
            .addPath("/statistics/$timeFrame")
            .addQueryParameters(queryParameters)
            .build()

        Amplify.API.get(API_NAME, request,
            {
                val result = it.data.asJSONObject().getJSONObject("data").toString()
                val gson = Gson()
                val statistics = gson.fromJson(result, StatisticsResponse::class.java)
                Log.i("MyAmplifyApp", "GET statistics succeeded: $statistics")
                onComplete(statistics)
            },
            {
                Log.e("MyAmplifyApp", "GET statistics failed.", it)
                onComplete(null)
            }
        )
    }

    fun getUser(id: String, onComplete: (ApiNodeUser?) -> Unit) {
        val queryParams = mapOf("id" to id)
        val request = RestOptions.builder()
            .addPath("/user/get")
            .addQueryParameters(queryParams)
            .build()

        Amplify.API.get(API_NAME, request,
            {
                println(it)
                val result = it.data.asJSONObject().toString()
                println(result)
                val gson = Gson()
                val user = gson.fromJson(result, ApiNodeUser::class.java)
                Log.i("MyAmplifyApp", "GET apiNodeUser succeeded: $user")
                onComplete(user)
            }, {
                Log.e("MyAmplifyApp", "GET apiNodeUser failed.", it)
                onComplete(null)
            })
    }

    fun createUser(user: ApiNodeUser, onComplete: (Boolean) -> Unit) {
        val getUserRequest = mapOf("item" to user)
        val gson = Gson()
        val body = gson.toJson(getUserRequest).toByteArray()
        val request = RestOptions.builder()
            .addPath("/user/create")
            .addBody(body)
            .build()
        println(getUserRequest)
        println(body)


        Amplify.API.post(API_NAME, request,
            {
                val result = gson.toJson(it.data)
                Log.i("MyAmplifyApp", "CREATE apiNodeUser succeeded: $result")
                onComplete(true)
            }, {
                Log.e("MyAmplifyApp", "CREATE apiNodeUser failed.", it)
                onComplete(false)
            })
    }

    fun fetchAuthUserAttributes(onComplete: (List<AuthUserAttribute>) -> Unit) {
        Amplify.Auth.fetchUserAttributes({ attributes ->
            Log.i("MyAmplifyApp", "User attributes: $attributes")
            onComplete(attributes)
        }, {
            Log.i("MyAmplifyApp", "Failed to fetch User attributes: $it")
        }
        )
    }

    private fun syncAuthAndApiUser(username: String, onComplete: (Boolean) -> Unit) {
        fetchAuthUserAttributes() { authUserAttributes ->
            if (authUserAttributes.isEmpty()) {
                onComplete(true)
            }

            val user = ApiNodeUser()
            user.id = username

            for (attribute in authUserAttributes) {
                when (attribute.key) {
                    AuthUserAttributeKey.email() -> user.email = attribute.value
                    AuthUserAttributeKey.name() -> user.name = attribute.value
                    AuthUserAttributeKey.familyName() -> user.familyName = attribute.value
                    AuthUserAttributeKey.custom("marketing") -> user.marketing = attribute.value == "1"
                }
            }

            getUser(username) { fetchedUser ->
                fetchedUser?.let {
                    if (fetchedUser.id == null) {
                        createUser(user) { success ->
                            println(user)
                            println("abracadabra")
                            onComplete(success)
                        }
                    } else {
                        updateUser(username, it) { success ->
                            onComplete(success)
                        }
                    }
                }
            }
        }
    }

    private fun updateUser(id: String, user: ApiNodeUser, onComplete: (Boolean) -> Unit) {
        val getUserRequest = mapOf("id" to id, "item" to user)
        val gson = Gson()
        val body = gson.toJson(getUserRequest).toByteArray()
        val request = RestOptions.builder()
            .addPath("/user/update")
            .addBody(body)
            .build()

        Amplify.API.post(API_NAME, request,
            {
                val result = it.data.asJSONObject().toString()
                Log.i("MyAmplifyApp", "UPDATE apiNodeUser succeeded: $result")
                onComplete(true)
            }, {
                Log.e("MyAmplifyApp", "UPDATE apiNodeUser failed.", it)
                onComplete(false)
            })
    }
}