package com.example.dualstride.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.dualstride.ui.theme.*

@Composable
fun RegisterScreen(
    onNavigateToLogin : () -> Unit,
    onRegisterSuccess : () -> Unit,
    viewModel         : AuthViewModel = hiltViewModel()
) {
    val uiState         by viewModel.uiState.collectAsStateWithLifecycle()
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPass        by remember { mutableStateOf(false) }
    var showConfirm     by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) { if (uiState.success) onRegisterSuccess() }

    Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onNavigateToLogin) {
                    Icon(Icons.Default.ArrowBack, null, tint = TextMuted)
                    Spacer(Modifier.width(4.dp))
                    Text("Back", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Create Account", style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Set up your GuardianWear profile", style = MaterialTheme.typography.bodySmall,
                color = TextMuted, modifier = Modifier.padding(top = 6.dp))
            Spacer(Modifier.height(36.dp))

            OutlinedTextField(value = email, onValueChange = { email = it },
                label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), colors = authFieldColors())

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(value = password, onValueChange = { password = it },
                label = { Text("Password") }, leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), colors = authFieldColors())

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") }, leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton(onClick = { showConfirm = !showConfirm }) {
                        Icon(if (showConfirm) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                    }
                },
                visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), colors = authFieldColors())

            uiState.error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = { viewModel.register(email, password, confirmPassword) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal400, contentColor = Color(0xFF003731))
            ) {
                if (uiState.isLoading)
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF003731), strokeWidth = 2.dp)
                else
                    Text("Create Account", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Sign In", color = Teal400,
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}