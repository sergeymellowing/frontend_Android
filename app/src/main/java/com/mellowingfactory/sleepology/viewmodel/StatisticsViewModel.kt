package com.mellowingfactory.sleepology.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mellowingfactory.sleepology.models.LoginState
import com.mellowingfactory.sleepology.models.Timeframe
import com.mellowingfactory.sleepology.services.AmplifyService
import com.mellowingfactory.sleepology.services.AmplifyServiceImpl
import java.util.*

class StatisticsViewModel: ViewModel() {
    private val amplifyService: AmplifyService = AmplifyServiceImpl()
    var journalDate = mutableStateOf(Date())
        private set

    var timeframe = mutableStateOf(Timeframe.weekly)
        private set

    fun getStatistics() {
        amplifyService.getStatistics(journalDate.value, timeframe.value) {
            // MARK: On complete closer
        }
    }
}