package com.example.dualstride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.dualstride.ui.navigation.GuardianNavGraph
import com.example.dualstride.ui.theme.GuardianWearTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuardianWearTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GuardianNavGraph()
                }
            }
        }
    }
}