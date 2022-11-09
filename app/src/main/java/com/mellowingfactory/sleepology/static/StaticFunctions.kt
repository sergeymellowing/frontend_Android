package com.mellowingfactory.sleepology.static

import android.icu.util.GregorianCalendar
import android.icu.util.TimeZone
import kotlin.math.abs

class Fun {
    companion object {
        fun getTimezoneOffset(): String {
            val tz = TimeZone.getDefault()
            val cal = GregorianCalendar.getInstance(tz)
            val offsetInMillis = tz.getOffset(cal. timeInMillis)

            return String.format("%02d", abs(offsetInMillis / 60000))
        }
    }
}

const val API_NAME = "DeviceApi"