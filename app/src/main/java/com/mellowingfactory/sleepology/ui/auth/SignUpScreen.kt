package com.mellowingfactory.sleepology.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.mellowingfactory.sleepology.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(viewModel: AuthViewModel) {
    val state by viewModel.signUpState

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(value = state.username,
            onValueChange = { viewModel.updateSignUpState(username = it) },
            placeholder = { Text(text = "Username") }
        )

        TextField(value = state.email,
            onValueChange = { viewModel.updateSignUpState(email = it) },
            placeholder = { Text(text = "Email") }
        )

        TextField(value = state.password,
            onValueChange = { viewModel.updateSignUpState(password = it) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            placeholder = { Text(text = "Password") }
        )

        Button(onClick = viewModel::signUp) {
            Text(text = "Sign Up")
        }

        TextButton(onClick = viewModel::showLogin) {
            Text(text = "Already have an account? Login")
        }
    }
}