package com.mellowingfactory.sleepology.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel

@Composable
fun ConfirmationScreen(viewModel: AuthViewModel) {
    val state by viewModel.verificationState

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = state.code,
            onValueChange = { viewModel.updateVerificationCodeState(code = it) },
            placeholder = { Text(text = "Verification Code") }
        )

        Button(onClick = viewModel::verifyCode) {
            Text(text = "Verify")
        }

    }
}