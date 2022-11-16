package com.mellowingfactory.sleepology.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel

@Composable
fun LoadingScreen(viewModel: AuthViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        CircularProgressIndicator()
    }
}