package com.monkhex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.monkhex.app.core.designsystem.MonkHexTheme
import com.monkhex.app.navigation.MonkHexNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonkHexTheme {
                MonkHexNavGraph()
            }
        }
    }
}

