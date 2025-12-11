package com.itvo.studycardsclean.presentation // <--- Asegúrate de que este paquete sea el correcto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.itvo.studycardsclean.presentation.ui.AppNavigation
import com.itvo.studycardsclean.presentation.ui.theme.StudyCardsCleanTheme // <--- Revisa que este nombre coincida con tu carpeta theme

@AndroidEntryPoint // <--- ¡ESTO ES LO MÁS IMPORTANTE! Sin esto, la app se cierra.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Asegúrate de que el nombre del tema sea el correcto (puede ser StudyCardsTheme o StudyCardsCleanTheme)
            StudyCardsCleanTheme {
                // Aquí llamamos a TU navegación, no al Greeting por defecto
                AppNavigation()
            }
        }
    }
}