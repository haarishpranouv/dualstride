package com.guardianwear.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guardianwear.app.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToRegister : () -> Unit,
    onLoginSuccess       : () -> Unit,
    viewModel            : AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) { if (uiState.success) onLoginSuccess() }

    Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(64.dp)
                    .background(Color(0x202DD4BF), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) { Text("❤", fontSize = 28.sp) }

            Spacer(Modifier.height(20.dp))

            Text("GuardianWear", style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Elderly health monitoring", style = MaterialTheme.typography.bodySmall,
                color = TextMuted, modifier = Modifier.padding(top = 4.dp))

            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), colors = authFieldColors()
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), colors = authFieldColors()
            )

            uiState.error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal400, contentColor = Color(0xFF003731))
            ) {
                if (uiState.isLoading)
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF003731), strokeWidth = 2.dp)
                else
                    Text("Sign In", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register", color = Teal400,
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = Teal400,
    unfocusedBorderColor    = BorderColor,
    focusedLabelColor       = Teal400,
    unfocusedLabelColor     = TextMuted,
    cursorColor             = Teal400,
    focusedTextColor        = TextPrimary,
    unfocusedTextColor      = TextPrimary,
    focusedContainerColor   = BgSurface2,
    unfocusedContainerColor = BgSurface2,
    focusedLeadingIconColor    = Teal400,
    unfocusedLeadingIconColor  = TextMuted,
    focusedTrailingIconColor   = TextMuted,
    unfocusedTrailingIconColor = TextMuted
)