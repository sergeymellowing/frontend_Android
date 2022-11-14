package com.mellowingfactory.sleepology.models

data class ApiNodeUser (
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var familyName: String? = null,
    var age: Int? = null,
    var height: Int? = null,
    var weight: Int? = null,
    var marketing: Boolean? = null,
    var gender: String? = null,
    var created: String? = null,
    var updated: String? = null,
    var offset: Int? = null
)

data class CreateApiNodeUserRequest (
    val item: ApiNodeUser
    )

data class GetApiNodeUserResponse (
    val data: ApiNodeUser
)

data class UpdateApiNodeUserRequest (
    val id: String,
    val item: ApiNodeUser
)