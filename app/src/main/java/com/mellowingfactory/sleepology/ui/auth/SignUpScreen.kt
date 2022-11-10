package com.mellowingfactory.sleepology.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.RadioButton
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
        TextField(value = state.name,
            onValueChange = { viewModel.updateSignUpState(name = it) },
            placeholder = { Text(text = "Name") }
        )

        TextField(value = state.familyName,
            onValueChange = { viewModel.updateSignUpState(familyName = it) },
            placeholder = { Text(text = "Family name") }
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


        RadioButton(
            selected = state.marketing,
            onClick = { viewModel.updateSignUpState(marketing = !state.marketing)  }
        )

        Button(onClick = viewModel::signUp) {
            Text(text = "Sign Up")
        }

        TextButton(onClick = viewModel::showLogin) {
            Text(text = "Already have an account? Login")
        }
    }
}