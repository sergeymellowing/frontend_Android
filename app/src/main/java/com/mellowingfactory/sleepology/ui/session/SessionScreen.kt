package com.mellowingfactory.sleepology.ui.session

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.google.gson.Gson
import com.mellowingfactory.sleepology.models.ApiNodeUser
import com.mellowingfactory.sleepology.models.Timeframe
import com.mellowingfactory.sleepology.models.UpdateApiNodeUserRequest
import com.mellowingfactory.sleepology.static.API_NAME
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel
import com.mellowingfactory.sleepology.viewmodel.DeviceViewModel
import com.mellowingfactory.sleepology.viewmodel.StatisticsViewModel
import com.mellowingfactory.sleepology.viewmodel.UserViewModel
import org.json.JSONObject
import java.util.*


@Composable
fun SessionScreen(viewModel: AuthViewModel, statisticsViewModel: StatisticsViewModel, userViewModel: UserViewModel, deviceViewModel: DeviceViewModel) {
    val mContext = LocalContext.current
    val scrollableState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(scrollableState),
    ) {
        Text(text = "YOU HAVE LOGGED IN")
        Button(onClick = viewModel::logOut, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
            Text(text = "Log Out", color = Color.White)
        }

        Spacer(modifier = Modifier.height(100.dp))

        val mDate = remember { mutableStateOf("") }
        var date: Calendar = Calendar.getInstance()
        var thisAYear = date.get(Calendar.YEAR)
        var thisAMonth = date.get(Calendar.MONTH)
        var thisADay = date.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { _, thisYear, thisMonth, thisDay ->
            // Display Selected date in textbox
            thisAMonth = thisMonth + 1
            thisADay = thisDay
            thisAYear = thisYear

            mDate.value = "Date: $thisAMonth/$thisDay/$thisYear"
            val newDate:Calendar =Calendar.getInstance()
            newDate.set(thisYear, thisMonth, thisDay)
            statisticsViewModel.journalDate.value = newDate.time // setting new date
        }, thisAYear, thisAMonth, thisADay)

            Button(onClick = {
                dpd.show()
            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
                Text(text = "Open Date Picker", color = Color.White)
            }

            Text(text = "Selected Date: ${statisticsViewModel.journalDate.value}", textAlign = TextAlign.Center)

        Button(onClick = {
            statisticsViewModel.getStatistics()
        }) {
            Text(text = "Get Main Statistics")
        }

        Button(onClick = {
            statisticsViewModel.getStatistics(date = statisticsViewModel.journalDate.value, timeframe = Timeframe.weekly)
        }) {
            Text(text = "Get Weekly Statistics")
        }

        Button(onClick = {
            statisticsViewModel.getStatistics(date = statisticsViewModel.journalDate.value, timeframe = Timeframe.monthly)
        }) {
            Text(text = "Get Monthly Statistics")
        }

        Button(onClick = {
            statisticsViewModel.getStatistics(date = statisticsViewModel.journalDate.value, timeframe = Timeframe.yearly)
        }) {
            Text(text = "Get Yearly Statistics")
        }

        Button(onClick = {
            statisticsViewModel.getJournal(statisticsViewModel.journalDate.value)
        }) {
            Text(text = "Get Journal")
        }


        Spacer(modifier = Modifier.height(100.dp))


        Button(onClick = { userViewModel.getUser(viewModel.authUser.value.username) }) {
            Text(text = "Get User")
        }

        Button(onClick = { userViewModel.fetchAuthUserAttributes() {} }) {
            Text(text = "Get User Attributes")
        }

        Button(onClick = {
            userViewModel.updateUser(viewModel.authUser.value.username, userViewModel.user.value)
        }) {
            Text(text = "Update User Manually")
        }

        Button(onClick = {
            userViewModel.deleteUser(viewModel.authUser.value.username)
        }) {
            Text(text = "Delete User")
        }


        Spacer(modifier = Modifier.height(100.dp))


        Button(onClick = { deviceViewModel.getDevice(viewModel.authUser.value.username) }) {
            Text(text = "Get Device")
        }

        Button(onClick = {
            val testingMode = 3
            println(userViewModel.user.value.id)
            userViewModel.user.value.id?.let {
                deviceViewModel.updateDevice(testingMode, it)
            }
        }) {
            Text(text = "Update Device")
        }
//
//        Button(onClick = {
//            userViewModel.deleteUser(viewModel.username.value)
//        }) {
//            Text(text = "Delete User")
//        }

        Button(onClick = { deviceViewModel.device.value.id?.let { deviceViewModel.getTimer(it) } }) {
            Text(text = "Get Timer")
        }

    }
}