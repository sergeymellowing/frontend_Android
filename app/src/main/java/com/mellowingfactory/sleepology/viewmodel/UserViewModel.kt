package com.mellowingfactory.sleepology.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.mellowingfactory.sleepology.models.ApiNodeUser
import com.mellowingfactory.sleepology.models.Timeframe
import com.mellowingfactory.sleepology.services.ApiNodeServer
import java.util.*

class UserViewModel: ViewModel() {
    private val apiNodeServer: ApiNodeServer = ApiNodeServer()
//    var user = mutableStateOf(ApiNodeUser())
//        private set

    var user = mutableStateOf(ApiNodeUser())
        private set

    fun getUser(username: String) {
        apiNodeServer.getUser(username) { fetchedUser ->
            if (fetchedUser != null) {
                user.value = fetchedUser
            }
        }
    }

    fun createUser(user: ApiNodeUser) {
        apiNodeServer.createUser(user) { bool ->
            // TODO: Do something with $bool
        }
    }

    fun updateUser(id: String, user: ApiNodeUser) {
        apiNodeServer.updateUser(id, user) { bool ->
            // TODO: Do something with $bool
        }
    }

    fun deleteUser(id: String) {
        apiNodeServer.deleteUser(id) { bool ->
            // TODO: Do something with $bool
        }
    }

    fun fetchAuthUserAttributes(onComplete: (List<AuthUserAttribute>) -> Unit) {
        apiNodeServer.fetchAuthUserAttributes { attributes ->
            onComplete(attributes)
            // TODO: Do something with $attributes
        }
    }

//    fun syncAuthAndApiUser(username: String, onComplete: (Boolean) -> Unit) {
//        fetchAuthUserAttributes() { authUserAttributes ->
//            if (authUserAttributes.isEmpty()) {
//                onComplete(true)
//            }
//
//            val user = ApiNodeUser()
//            user.id = username
//
//            for (attribute in authUserAttributes) {
//                user.email = AuthUserAttributeKey.email().toString()
//                user.name =  AuthUserAttributeKey.name().toString()
//                user.familyName = AuthUserAttributeKey.familyName().toString()
//                user.marketing = AuthUserAttributeKey.custom("marketing").toString() == "1"
//            }
//
//            apiNodeServer.getUser(username) { user ->
//                user?.let {
//                    if (user.id == null) {
//                        apiNodeServer.createUser(user) { success ->
//                            onComplete(success)
//                        }
//                    } else {
//                        apiNodeServer.updateUser(username, it) { success ->
//                            onComplete(success)
//                        }
//                    }
//                }
//            }
//        }
//    }
}