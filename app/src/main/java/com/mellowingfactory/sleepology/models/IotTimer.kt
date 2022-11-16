package com.mellowingfactory.sleepology.models

import com.mellowingfactory.sleepology.static.DEFAULT_DURATION_MINUTES

data class IotTimer (
    var targetTime: Int? = null,
    var week: List<Boolean> = listOf(true, true, true, true, true, true, true),
    var timezone_offset: Int? = null,
    var duration: Int = DEFAULT_DURATION_MINUTES,
    var d_id: String? = null,
    var t_id: String? = null,
    var lastActioned: String? = null,

    var created: String? = null,
    var updated: String? = null,

    var isActive: Boolean? = null,
    var isSnoozed: Boolean? = null,

    var lastSnooze: String? = null,
    var isSuppressed: Boolean? = null,
        )