package com.example.waracletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.waracletest.presentation.CakeListScreen
import com.example.waracletest.ui.theme.WaracletestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaracletestTheme {
                CakeListScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
