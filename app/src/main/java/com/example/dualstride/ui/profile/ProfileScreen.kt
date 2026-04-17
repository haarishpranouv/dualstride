package com.guardianwear.app.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guardianwear.app.ui.theme.*

@Composable
fun ProfileScreen(onLogout: () -> Unit, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val p     = state.profile
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.saved) { if (state.saved) viewModel.clearSaved() }

    Column(modifier = Modifier.fillMaxSize().background(BgPrimary)
        .verticalScroll(rememberScrollState()).padding(16.dp)) {

        Text("Profile", style = MaterialTheme.typography.titleLarge,
            color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(viewModel.currentEmail, style = MaterialTheme.typography.bodySmall, color = TextMuted)

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(80.dp)
                .background(Color(0x202DD4BF), CircleShape)
                .border(2.dp, Teal400.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center) {
                Text(p.name.take(1).uppercase().ifBlank { "?" },
                    fontSize = 32.sp, color = Teal400, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))
        ProfileSectionTitle("Personal Information")
        Spacer(Modifier.height(10.dp))

        ProfileField("Full Name", p.name, Icons.Default.Person) {
            viewModel.updateProfile(p.copy(name = it)) }
        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                ProfileField("Age", if (p.age == 0) "" else p.age.toString(),
                    Icons.Default.Cake, KeyboardType.Number) {
                    viewModel.updateProfile(p.copy(age = it.toIntOrNull() ?: 0)) }
            }
            Box(modifier = Modifier.weight(1f)) {
                ProfileField("Blood Group", p.bloodGroup, Icons.Default.Bloodtype) {
                    viewModel.updateProfile(p.copy(bloodGroup = it)) }
            }
        }

        Spacer(Modifier.height(20.dp))
        ProfileSectionTitle("Emergency Contact")
        Spacer(Modifier.height(10.dp))

        ProfileField("Contact Name", p.emergencyName, Icons.Default.ContactPhone) {
            viewModel.updateProfile(p.copy(emergencyName = it)) }
        Spacer(Modifier.height(10.dp))
        ProfileField("Phone Number", p.emergencyPhone, Icons.Default.Phone, KeyboardType.Phone) {
            viewModel.updateProfile(p.copy(emergencyPhone = it)) }

        Spacer(Modifier.height(20.dp))
        ProfileSectionTitle("Medical Conditions")
        Spacer(Modifier.height(10.dp))

        val conditions = listOf("Hypertension","Diabetes","Cardiac Issues","Parkinson's","Arthritis","Osteoporosis")
        Column(modifier = Modifier.fillMaxWidth()
            .background(BgSurface2, RoundedCornerShape(14.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            conditions.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { condition ->
                        val selected = condition in p.conditions
                        Row(modifier = Modifier.weight(1f)
                            .background(if (selected) Color(0x202DD4BF) else BgSurface3, RoundedCornerShape(8.dp))
                            .border(1.dp, if (selected) Teal400.copy(0.4f) else BorderColor, RoundedCornerShape(8.dp))
                            .clickable {
                                val updated = if (selected) p.conditions - condition else p.conditions + condition
                                viewModel.updateProfile(p.copy(conditions = updated))
                            }.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                null, tint = if (selected) Teal400 else TextMuted, modifier = Modifier.size(14.dp))
                            Text(condition, fontSize = 11.sp, color = if (selected) Teal400 else TextMuted)
                        }
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { viewModel.saveProfile() }, enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal400, contentColor = Color(0xFF003731))
        ) {
            if (state.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp),
                color = Color(0xFF003731), strokeWidth = 2.dp)
            else {
                Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Save Profile", fontWeight = FontWeight.SemiBold)
            }
        }

        if (state.saved) { Spacer(Modifier.height(8.dp))
            Text("✅ Profile saved successfully", color = Green400,
                style = MaterialTheme.typography.bodySmall) }

        state.error?.let { Spacer(Modifier.height(8.dp))
            Text(it, color = Red400, style = MaterialTheme.typography.bodySmall) }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Red400.copy(alpha = 0.4f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Red400)
        ) {
            Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Sign Out", fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(80.dp))
    }

    if (showLogoutDialog) {
        AlertDialog(onDismissRequest = { showLogoutDialog = false },
            containerColor = BgSurface2,
            title   = { Text("Sign Out", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
            text    = { Text("Are you sure you want to sign out?", color = TextMuted) },
            confirmButton = {
                TextButton(onClick = { viewModel.logout(); onLogout() }) {
                    Text("Sign Out", color = Red400, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun ProfileSectionTitle(text: String) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelSmall,
        color = TextMuted, fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
}

@Composable
fun ProfileField(label: String, value: String, icon: ImageVector,
                 keyboardType: KeyboardType = KeyboardType.Text, onChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onChange,
        label = { Text(label) }, leadingIcon = { Icon(icon, null) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = Teal400, unfocusedBorderColor    = BorderColor,
            focusedLabelColor       = Teal400, unfocusedLabelColor     = TextMuted,
            cursorColor             = Teal400, focusedTextColor        = TextPrimary,
            unfocusedTextColor      = TextPrimary,
            focusedContainerColor   = BgSurface2, unfocusedContainerColor = BgSurface2,
            focusedLeadingIconColor = Teal400, unfocusedLeadingIconColor = TextMuted
        )
    )
}