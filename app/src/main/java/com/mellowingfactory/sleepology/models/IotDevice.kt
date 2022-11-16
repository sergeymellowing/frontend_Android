package com.mellowingfactory.sleepology.models

data class IotDevice(
    var id: String? = null,
    var rev: Int? = null,
    var fv: Int? = null,
    var created: String? = null,
    var updated: String? = null,
    var mode: Int? = null
)

data class CreateIotDeviceRequest (
    val u_id: String,
    val item: IotDevice
)

data class UpdateIotDeviceRequest (
    val d_id: String,
    val item: UpdateBody
)

data class UpdateBody(
    var name: String,
    var rev: Int,
    var fv: Int,
    var mode: Int?
)
