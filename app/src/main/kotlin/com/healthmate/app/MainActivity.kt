package com.healthmate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.healthmate.app.navigation.HealthMateApp
import com.healthmate.app.ui.theme.HealthMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize the database helper to trigger onCreate() on first launch
        val dbHelper = com.healthmate.app.data.local.HealthDatabaseHelper(this)
        dbHelper.writableDatabase // Accessing this property forces database creation

        setContent {
            HealthMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HealthMateApp()
                }
            }
        }
    }
}