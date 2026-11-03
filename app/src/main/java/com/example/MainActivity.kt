package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.ViralViewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.ViralIQTheme
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {

    private val viralViewModel: ViralViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Authoritative App Check initialization with launch intent BEFORE UI or Firebase ops
        configureAppCheck(this, intent)

        // STEP 2: Modern edge-to-edge system insets
        enableEdgeToEdge()

        // STEP 3: Compose UI Content
        setContent {
            ViralIQTheme {
                HomeScreen(viewModel = viralViewModel)
            }
        }
    }
}
