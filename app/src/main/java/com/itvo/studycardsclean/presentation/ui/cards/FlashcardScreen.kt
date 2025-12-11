package com.itvo.studycardsclean.presentation.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itvo.studycardsclean.presentation.viewmodel.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class) // CORRECCIÓN 1: Agregado OptIn
@Composable
fun FlashcardScreen(
    topicId: Int,
    topicColorHex: String,
    onBackClick: () -> Unit,
    // CORRECCIÓN 2: Usamos hiltViewModel()
    viewModel: FlashcardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(topicColorHex))
    } catch (e: Exception) { Color.White }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // CORRECCIÓN 3: Texto fijo para evitar error de 'currentTopic'
                    Text("Estudiando", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = backgroundColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(backgroundColor).padding(16.dp)
        ) {
            if (uiState.cards.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tarjetas aquí.", color = Color.DarkGray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                    items(uiState.cards) { card ->
                        FlipCard(cardData = card)
                    }
                }
            }
        }
    }
}