package com.mellowingfactory.sleepology.models

data class StatisticsResponse (
    var created: String?,
    var sleepStages: SleepStageResult,
    var radarValues: List<List<Double>>,
    var heartRate: BiosignalResult,
    var breathingRate: BiosignalResult,
    var sleepQuality: List<Int>,
    var sleepLatency: List<Int>,
    var lightDuration: List<Int>,
    var sleepEfficiency: List<Int>,
    var deepDuration: List<Int>,
    var remDuration: List<Int>,
    var wokenDuration: List<Int>,
    var wakeUpState: List<Int>
)

data class SleepStageResult (
    var sleepStart: List<Double>,
    var sleepEnd: List<Double>,
    var sleepDuration: List<Int>,
    var sleepStages: List<List<Int>>
    )

data class BiosignalResult (
    var values: List<Double>,
    var variability: List<Double>,
    var maxValue: List<Double>,
    var minValue: List<Double>
)

data class QueryStatisticsDataResponse (
    var data: StatisticsResponse
    )

