package com.mellowingfactory.sleepology.models

data class SignUpState(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var familyName: String = "",
    var marketing: Boolean = true
)

data class LoginState(
    var username: String = "",
    var password: String = ""
)

data class VerificationState(
    var username: String = "",
    var code: String = ""
)