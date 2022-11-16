package com.mellowingfactory.sleepology.services

import android.content.Context
import android.util.Log
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mellowingfactory.sleepology.models.*
import com.mellowingfactory.sleepology.static.API_NAME
import com.mellowingfactory.sleepology.static.Fun
import java.lang.reflect.Type
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

    fun fetchCurrentAuthSession(onComplete: (AuthUser?, AuthSession) -> Unit) {
        Amplify.Auth.fetchAuthSession(
            {
                val session = it as AWSCognitoAuthSession
                when (session.identityId.type) {
                    AuthSessionResult.Type.SUCCESS -> {
                        Log.i("AuthQuickStart", "IdentityId = ${session.identityId.value}")
                        // TODO: tokens for recording sound and upload to the server
                        val tokens = session.userPoolTokens.value
                        println("access token: ${tokens?.accessToken}")
                        println("idToken: ${tokens?.idToken}")
                        println("refresh Token: ${tokens?.refreshToken}")
                        val user = Amplify.Auth.currentUser
                        onComplete(user, session)
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
        timeFrame: Timeframe? = Timeframe.weekly,
        onComplete: (StatisticsResponse?) -> Unit
    ) {
        val timeZoneOffset = Fun.getTimezoneOffset()

        var journalDateString: String? = null
        if (journalDate != null) {
            val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(journalDate)
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

    fun getJournal(
        journalDate: Date?,
        onComplete: (JournalResponse?) -> Unit
    ) {
        val timeZoneOffset = Fun.getTimezoneOffset()

        var journalDateString: String? = null
        if (journalDate != null) {
            val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(journalDate)
            journalDateString = formattedDate.toString()
        }


        val queryParameters = mapOf(
            "journalDate" to journalDateString,
            "timeZoneOffset" to timeZoneOffset
        )

        println("queryParameters: $queryParameters")

        val request = RestOptions.builder()
            .addPath("/statistics/journal/query")
            .addQueryParameters(queryParameters)
            .build()

        Amplify.API.get(API_NAME, request,
            {
                val result = it.data.asJSONObject().getJSONObject("data").toString()
                val gson = Gson()
                val journal = gson.fromJson(result, JournalResponse::class.java)
                Log.i("MyAmplifyApp", "GET journal succeeded: $journal")
                onComplete(journal)
            },
            {
                Log.e("MyAmplifyApp", "GET journal failed.", it)
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
                val userResponse = gson.fromJson(result, GetApiNodeUserResponse::class.java)

                Log.i("MyAmplifyApp", "GET apiNodeUser succeeded: $userResponse")
                onComplete(userResponse.data)
            }, {
                Log.e("MyAmplifyApp", "GET apiNodeUser failed.", it)
                onComplete(null)
            })
    }

    fun updateUser(id: String, user: ApiNodeUser, onComplete: (Boolean) -> Unit) {
        val updateApiNodeUserRequest = UpdateApiNodeUserRequest(id, user)
        val gson = Gson()
        val jsonObject = gson.toJson(updateApiNodeUserRequest)
        val body = jsonObject.toString().toByteArray()
        println("updateApiNodeUserRequest $updateApiNodeUserRequest")
        println("jsonObject $jsonObject")
        println("body ${String(body, Charsets.UTF_8)}")
        val request = RestOptions.builder()
            .addPath("/user/update")
            .addBody(body)
            .build()


        Amplify.API.post(API_NAME, request,
            {
//                println("request data:")
//                println(String(request.data))
                val result = it.data.asJSONObject().toString()
                Log.i("MyAmplifyApp", "UPDATE apiNodeUser succeeded: $result")
                onComplete(true)
            }, {
                Log.e("MyAmplifyApp", "UPDATE apiNodeUser failed.", it)
                onComplete(false)
            })
    }

    fun createUser(user: ApiNodeUser, onComplete: (Boolean) -> Unit) {
        val getUserRequest = CreateApiNodeUserRequest(item = user)
        val gson = Gson()
        val jsonObject = gson.toJson(getUserRequest)
        val body = jsonObject.toString().toByteArray()
        val request = RestOptions.builder()
            .addPath("/user/create")
            .addBody(body)
            .build()

        println("getUserRequest $getUserRequest")
        println("jsonObject $jsonObject")

        try {
            Amplify.API.post(API_NAME, request,
                {
                    val result = it.toString()
                    val code = it.code
                    println("code $result")
                    println("code $code")
                    Log.i("MyAmplifyApp", "CREATE apiNodeUser succeeded: $result")
                    onComplete(true)
                }, {
                    Log.e("MyAmplifyApp", "CREATE apiNodeUser failed.", it)
                    onComplete(false)
                })
        } catch (error: ApiException) {
            Log.e("MyAmplifyApp", "CREATE apiNodeUser failed.", error)
        }
    }

    fun deleteUser(id: String, onComplete: (Boolean) -> Unit) {
        val queryParameters = mapOf("id" to id)
        val request = RestOptions.builder()
            .addPath("/user/delete")
            .addQueryParameters(queryParameters)
            .build()

        Amplify.API.delete(API_NAME, request,
            {
                val result = it.toString()
                val code = it.code
                println("code $result")
                println("code $code")
                // TODO: getting error code 404. Permissions
                Log.i("MyAmplifyApp", "DELETE DB user succeeded: $it")
                Amplify.Auth.deleteUser(
                    {
                        logOut { onComplete(true) }
                        Log.i("AuthQuickStart", "Delete Cognito user succeeded")
                    },
                    {
                        Log.e("AuthQuickStart", "Delete Cognito user failed with error", it)
                        onComplete(false)
                    }
                )
            },
            {
                Log.e("MyAmplifyApp", "DELETE DB user failed.", it)
                onComplete(false)
            }
        )


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
                        updateUser(id = username, user = user) { success ->
                            onComplete(success)
                        }
                    }
                }
            }
        }
    }

    fun getDevice(id: String, onComplete: (IotDevice?) -> Unit) {
        if (id.isNotEmpty()) {
            val queryParameters = mapOf("u_id" to id)
            val request = RestOptions.builder()
                .addPath("/device/query")
                .addQueryParameters(queryParameters)
                .build()

            println(queryParameters)

            Amplify.API.get(API_NAME, request,
                {
                    val result = it.data.asJSONObject().getJSONArray("data")
                    if (result.length() > 0) {
                        val jsonObject = result.getJSONObject(0).toString()
                        val gson = Gson()
                        val device = gson.fromJson(jsonObject, IotDevice::class.java)
                        Log.i("MyAmplifyApp", "GET device succeeded: $device")
                        onComplete(device)
                    } else {
                        Log.i("MyAmplifyApp", "GET device succeeded. But device list is empty")
                        onComplete(null)
                    }
                },
                {
                    Log.e("MyAmplifyApp", "GET device failed.", it)
                    onComplete(null)
                }
            )
        }

    }

    fun updateDevice(id: String, device: IotDevice, onComplete: (Boolean) -> Unit) {
        val updateIotDeviceRequest = UpdateIotDeviceRequest(device.id!!, UpdateBody(id, device.rev!!, device.fv!!, device.mode))
        val gson = Gson()
        val jsonObject = gson.toJson(updateIotDeviceRequest)
        val body = jsonObject.toString().toByteArray()
        println("updateIotDeviceRequest $updateIotDeviceRequest")
        println("jsonObject $jsonObject")
        val request = RestOptions.builder()
            .addPath("/device/update")
            .addBody(body)
            .build()

        Amplify.API.post(API_NAME, request,
            {
                val result = it.data.asJSONObject().toString()
                Log.i("MyAmplifyApp", "UPDATE device succeeded: $result")
                onComplete(true)
            }, {
                Log.e("MyAmplifyApp", "UPDATE device failed.", it)
                onComplete(false)
            })
    }

    fun getTimer(d_id: String, onComplete: (List<IotTimer>?) -> Unit) {
        val queryParameters = mapOf("d_id" to d_id)
        val request = RestOptions.builder()
            .addPath("/timer/query")
            .addQueryParameters(queryParameters)
            .build()

        println(queryParameters)

        Amplify.API.get(API_NAME, request,
            {
                val result = it.data.asJSONObject().getJSONArray("data")
                if (result.length() > 0) {
                    val gson = Gson()
                    val listType: Type = object : TypeToken<ArrayList<IotTimer?>?>() {}.type
                    val timers: List<IotTimer> = gson.fromJson(result.toString(), listType)
                    Log.i("MyAmplifyApp", "GET timer succeeded: $timers")
                    onComplete(timers)
                } else {
                    Log.i("MyAmplifyApp", "GET timer succeeded. But timer list is empty")
                    onComplete(null)
                }
            },
            {
                Log.e("MyAmplifyApp", "GET timer failed.", it)
                onComplete(null)
            }
        )
    }
}