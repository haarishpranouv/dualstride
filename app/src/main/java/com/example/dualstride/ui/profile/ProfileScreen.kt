package com.example.dualstride.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dualstride.data.model.UserProfile
import com.example.dualstride.ui.theme.*

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.titleLarge,
            color = TextPrimary, fontWeight = FontWeight.Bold)
        Text("Manage your personal information", style = MaterialTheme.typography.bodySmall, color = TextMuted)

        Spacer(Modifier.height(24.dp))

        // Profile Header
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(100.dp).background(BgSurface2, CircleShape).border(2.dp, Teal400, CircleShape),
                contentAlignment = Alignment.Center) {
                Text(state.profile.name.take(1).ifEmpty { "?" }, fontSize = 40.sp, color = Teal400, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))

        ProfileField("Full Name", state.profile.name, Icons.Default.Person) {
            viewModel.updateProfile(state.profile.copy(name = it))
        }
        ProfileField("Age", state.profile.age.toString(), Icons.Default.Cake) {
            viewModel.updateProfile(state.profile.copy(age = it.toIntOrNull() ?: 0))
        }
        ProfileField("Blood Group", state.profile.bloodGroup, Icons.Default.Bloodtype) {
            viewModel.updateProfile(state.profile.copy(bloodGroup = it))
        }

        Spacer(Modifier.height(16.dp))
        Text("Emergency Contact", style = MaterialTheme.typography.labelMedium, color = Teal400, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        ProfileField("Contact Name", state.profile.emergencyName, Icons.Default.ContactPhone) {
            viewModel.updateProfile(state.profile.copy(emergencyName = it))
        }
        ProfileField("Contact Phone", state.profile.emergencyPhone, Icons.Default.Phone) {
            viewModel.updateProfile(state.profile.copy(emergencyPhone = it))
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.saveProfile() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal400),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            else Text("Save Profile", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { viewModel.logout() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Red400),
            border = BorderStroke(1.dp, Red400)
        ) {
            Text("Logout", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(80.dp))
    }

    if (state.saved) {
        LaunchedEffect(Unit) {
            // Show toast or snackbar
            viewModel.clearSaved()
        }
    }
}

@Composable
fun ProfileField(label: String, value: String, icon: ImageVector, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            leadingIcon = { Icon(icon, null, tint = Teal400) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BgSurface2,
                unfocusedContainerColor = BgSurface2,
                disabledContainerColor = BgSurface2,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )
    }
}