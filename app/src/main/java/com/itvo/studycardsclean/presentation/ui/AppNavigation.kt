package com.itvo.studycardsclean.presentation.ui

import android.net.Uri // <--- Necesario para proteger el código de color (#)
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.itvo.studycardsclean.presentation.ui.cards.FlashcardScreen
import com.itvo.studycardsclean.presentation.ui.home.HomeScreen
import com.itvo.studycardsclean.presentation.ui.subject.SubjectScreen

enum class AppScreens {
    HomeScreen,
    SubjectScreen,
    CardScreen
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.name
    ) {
        // 1. Pantalla Principal
        composable(route = AppScreens.HomeScreen.name) {
            HomeScreen(
                onSubjectClick = { subjectId ->
                    navController.navigate("${AppScreens.SubjectScreen.name}/$subjectId")
                },
                onSearchResultClick = { topicId ->
                    // Si buscamos directo, pasamos blanco por defecto
                    val defaultColor = Uri.encode("#FFFFFF")
                    navController.navigate("${AppScreens.CardScreen.name}/$topicId/$defaultColor")
                }
            )
        }

        // 2. Pantalla de la Materia (AQUÍ ESTÁ EL ARREGLO)
        composable(
            route = "${AppScreens.SubjectScreen.name}/{subjectId}",
            arguments = listOf(navArgument("subjectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getInt("subjectId") ?: 0

            SubjectScreen(
                subjectId = subjectId,
                onBackClick = { navController.popBackStack() },

                // --- CAMBIO IMPORTANTE ---
                // Ahora recibimos DOS cosas: el ID del tema y su Color
                onTopicClick = { topicId, topicColor ->
                    val encodedColor = Uri.encode(topicColor)
                    navController.navigate("${AppScreens.CardScreen.name}/$topicId/$encodedColor")
                }
            )
        }

        // 3. Pantalla de Tarjetas (Flashcards)
        composable(
            route = "${AppScreens.CardScreen.name}/{topicId}/{topicColor}",
            arguments = listOf(
                navArgument("topicId") { type = NavType.IntType },
                navArgument("topicColor") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getInt("topicId") ?: 0
            val topicColor = backStackEntry.arguments?.getString("topicColor") ?: "#FFFFFF"

            FlashcardScreen(
                topicId = topicId,
                topicColorHex = topicColor,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}