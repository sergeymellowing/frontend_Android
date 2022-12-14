package com.mellowingfactory.sleepology.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mellowingfactory.sleepology.models.Timeframe
import com.mellowingfactory.sleepology.services.ApiNodeServer
import java.util.*

class StatisticsViewModel: ViewModel() {
    private val apiNodeServer: ApiNodeServer = ApiNodeServer()
    var journalDate = mutableStateOf(Date())
        private set

    var timeframe = mutableStateOf(Timeframe.weekly)
        private set

    fun getStatistics(date: Date? = null, timeframe: Timeframe? = Timeframe.weekly) {
        apiNodeServer.getStatistics(date, timeframe) { statistics ->
            // TODO: Do something with $statistics
        }
    }

    fun getJournal(date: Date) {
        apiNodeServer.getJournal(date) { journal ->
            // TODO: Do something with $journal
        }
    }
}