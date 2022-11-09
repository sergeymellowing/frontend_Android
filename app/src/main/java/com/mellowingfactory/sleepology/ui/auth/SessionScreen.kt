package com.mellowingfactory.sleepology.ui.auth

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel
import com.mellowingfactory.sleepology.viewmodel.StatisticsViewModel


@Composable
fun SessionScreen(viewModel: AuthViewModel, statisticsViewModel: StatisticsViewModel) {
    val scrollableState = ScrollableState { 1f }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state = scrollableState, orientation = Orientation.Vertical),
    ) {
        Text(text = "YOU HAVE LOGGED IN")
        Button(onClick = viewModel::logOut) {
            Text(text = "Log Out")
        }

        Spacer(modifier = Modifier.height(100.dp))

        // TODO: pass journalDate
        // TODO: pass timeframe
        Button(onClick = statisticsViewModel::getStatistics) {
            Text(text = "Get Statistics")
        }

    }
}