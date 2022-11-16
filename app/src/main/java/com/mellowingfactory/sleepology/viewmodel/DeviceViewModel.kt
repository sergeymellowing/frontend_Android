package com.mellowingfactory.sleepology.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mellowingfactory.sleepology.models.IotDevice
import com.mellowingfactory.sleepology.models.IotTimer
import com.mellowingfactory.sleepology.services.ApiNodeServer

class DeviceViewModel: ViewModel() {
    private val apiNodeServer: ApiNodeServer = ApiNodeServer()

    var device = mutableStateOf(IotDevice())
        private set



    var timers = mutableStateListOf<IotTimer>()
        private set

    fun getDevice(username: String) {
        apiNodeServer.getDevice(username) { fetchedDevice ->
            if (fetchedDevice != null) {
                device.value = fetchedDevice
            }
        }
    }

    fun updateDevice(mode: Int, username: String) {
        val myDevice = device.value
        myDevice.mode = mode
        apiNodeServer.updateDevice(username, myDevice) {
            // TODO: handle boolean response
        }
    }

    fun getTimer(d_id: String) {
        apiNodeServer.getTimer(d_id){ fetchedTimers ->
            if (fetchedTimers != null) {
                timers.clear()
                for (timer in fetchedTimers) {
                    timers.add(timer)
                }
            }
        }
    }

}