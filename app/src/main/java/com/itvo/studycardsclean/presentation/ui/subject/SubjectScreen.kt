package com.itvo.studycardsclean.presentation.ui.subject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.Topic
import com.itvo.studycardsclean.presentation.ui.components.bounceClick
import com.itvo.studycardsclean.presentation.viewmodel.SubjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    subjectId: Int,
    onBackClick: () -> Unit,
    onTopicClick: (Int, String) -> Unit,
    viewModel: SubjectViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var topicToEdit by remember { mutableStateOf<Topic?>(null) }
    var initialCardsForEdit by remember { mutableStateOf<List<Card>>(emptyList()) }

    // CORRECCIÓN 1: Definimos la variable de color AQUÍ ARRIBA
    val themeColorHex = uiState.currentSubject?.colorHex ?: "#FFFFFF"

    val themeColor = try {
        Color(android.graphics.Color.parseColor(themeColorHex))
    } catch (e: Exception) { Color.Black }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.currentSubject?.name ?: "Cargando...",
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    topicToEdit = null
                    initialCardsForEdit = emptyList()
                    showForm = true
                },
                containerColor = themeColor,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, "Agregar Tema")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White).padding(horizontal = 16.dp)) {
            if (uiState.topics.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("¡Está vacío! Usa el botón +", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)) {
                    items(uiState.topics) { topic ->
                        TopicItem(
                            topic = topic,
                            // CORRECCIÓN 2: Ahora themeColorHex existe y se puede usar
                            onClick = { onTopicClick(topic.id, themeColorHex) },
                            onEdit = {
                                // CORRECCIÓN 3: Ahora viewModel tiene esta función
                                viewModel.getTopicDetails(topic.id) { cards ->
                                    topicToEdit = topic
                                    initialCardsForEdit = cards
                                    showForm = true
                                }
                            },
                            onDelete = { viewModel.deleteTopic(topic) }
                        )
                    }
                }
            }
        }
    }

    if (showForm) {
        TopicFormDialog(
            topicToEdit = topicToEdit,
            initialCards = initialCardsForEdit,
            onDismiss = { showForm = false },
            onSave = { name, cards ->
                // CORRECCIÓN 4: Usamos 'cards = cards' (no cardsToSave)
                viewModel.saveTopicWithCards(topicId = topicToEdit?.id ?: 0, topicName = name, cards = cards)
                showForm = false
            }
        )
    }
}

@Composable
fun TopicItem(topic: Topic, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth().bounceClick { onClick() }
    ) {
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = topic.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Box {
                IconButton(onClick = { expanded = true }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.MoreVert, "Opciones")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = Color.White) {
                    DropdownMenuItem(text = { Text("Editar") }, onClick = { expanded = false; onEdit() }, leadingIcon = { Icon(Icons.Default.Edit, null) })
                    DropdownMenuItem(text = { Text("Eliminar") }, onClick = { expanded = false; onDelete() }, leadingIcon = { Icon(Icons.Default.Delete, null) })
                }
            }
        }
    }
}