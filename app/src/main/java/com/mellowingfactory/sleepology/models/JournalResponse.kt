package com.mellowingfactory.sleepology.models

data class JournalResponse (
    var hasPremium: Boolean,
    var journalDate: String,
    var sleepQuality: Int,
    var sleepEfficiency: Int,
    var lightDuration: Int,
    var deepDuration: Int,
    var remDuration: Int,
    var wokenDuration: Int,
    var heartRateResult: BiosignalResult,
    var breathingRateResult: BiosignalResult,
    var sleepStageResult: SleepStageResult,
    var radarValues: List<Double>,
    var xAxisSteps: List<String>,
    var dates: List<Boolean>,
    var alarmOnTime: Int,
    var targetTime: Int,
    var timerDifference: Int,
    var wakeUpState: Int
)