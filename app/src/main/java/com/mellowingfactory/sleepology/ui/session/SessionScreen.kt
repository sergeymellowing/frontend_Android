package com.mellowingfactory.sleepology.ui.auth

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
import com.mellowingfactory.sleepology.viewmodel.StatisticsViewModel
import com.mellowingfactory.sleepology.viewmodel.UserViewModel
import org.json.JSONObject
import java.util.*


@Composable
fun SessionScreen(viewModel: AuthViewModel, statisticsViewModel: StatisticsViewModel, userViewModel: UserViewModel) {
    val mContext = LocalContext.current
    val scrollableState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(scrollableState),
    ) {
        Text(text = "YOU HAVE LOGGED IN")
        Button(onClick = viewModel::logOut) {
            Text(text = "Log Out")
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

//        val mYear: Int
//        val mMonth: Int
//        val mDay: Int
//        val mCalendar = Calendar.getInstance()
//        mYear = mCalendar.get(Calendar.YEAR)
//        mMonth = mCalendar.get(Calendar.MONTH)
//        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
//        mCalendar.time = Date()
//        val mDate = remember { mutableStateOf("") }
//
//        val mDatePickerDialog = DatePickerDialog(mContext,
//            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
//
////                mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
//            }, mYear, mMonth, mDay
//        )
//

            Button(onClick = {
                dpd.show()
            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
                Text(text = "Open Date Picker", color = Color.White)
            }

            Text(text = "Selected Date: ${statisticsViewModel.journalDate.value}", fontSize = 30.sp, textAlign = TextAlign.Center)

        // TODO: pass journalDate
        // TODO: pass timeframe
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


        Spacer(modifier = Modifier.height(100.dp))


        Button(onClick = { userViewModel.getUser(viewModel.username.value) }) {
            Text(text = "Get User")
        }

        Button(onClick = { userViewModel.fetchAuthUserAttributes() {} }) {
            Text(text = "Get User Attributes")
        }

        Button(onClick = {
            userViewModel.updateUser(viewModel.username.value, userViewModel.user.value)
        }) {
            Text(text = "Update User Manually")
        }
    }
}