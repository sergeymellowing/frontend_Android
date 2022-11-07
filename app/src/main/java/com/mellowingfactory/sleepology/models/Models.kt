package com.mellowingfactory.sleepology.models

data class SignUpState(
    var username: String = "",
    var email: String = "",
    var password: String = ""
)

data class LoginState(
    var username: String = "",
    var password: String = ""
)

data class VerificationState(
    var username: String = "",
    var code: String = ""
)