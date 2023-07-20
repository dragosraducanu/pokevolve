package com.dragos.pokevolve.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dragos.pokevolve.ui.Navigation
import com.dragos.pokevolve.ui.theme.PokeVolveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeVolveTheme {
                // A surface container using the 'background' color from the theme
                Navigation()
            }
        }
    }
}