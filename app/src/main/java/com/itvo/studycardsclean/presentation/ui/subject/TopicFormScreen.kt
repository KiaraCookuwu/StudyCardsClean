package com.itvo.studycardsclean.presentation.ui.subject

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.Topic

@Composable
fun TopicFormDialog(
    topicToEdit: Topic? = null,
    initialCards: List<Card> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (String, List<Card>) -> Unit
) {
    var topicName by remember { mutableStateOf(topicToEdit?.name ?: "") }
    val tempCards = remember { mutableStateListOf<Card>().apply { addAll(initialCards) } }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // CAMBIO AQUÍ: color = Color.White (Antes era amarillo pastel)
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                // --- ENCABEZADO ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                    Text(
                        text = if (topicToEdit == null) "Crear Nuevo Tema" else "Editar Tema",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = {
                        if (topicName.isNotBlank()) {
                            onSave(topicName, tempCards)
                        }
                    }) {
                        Text("GUARDAR", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- CAMPO NOMBRE DEL TEMA ---
                OutlinedTextField(
                    value = topicName,
                    onValueChange = { topicName = it },
                    label = { Text("Nombre del Tema / Unidad") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Tarjetas de estudio:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // --- LISTA DE TARJETAS ---
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(tempCards) { index, card ->
                        CardInputItem(
                            card = card,
                            onUpdate = { updatedCard -> tempCards[index] = updatedCard },
                            onRemove = { tempCards.removeAt(index) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // --- BOTÓN FLOTANTE ---
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        tempCards.add(Card(term = "", definition = "", topicId = 0))
                    },
                    containerColor = Color(0xFFA2CFFE), // Azul pastel para destacar
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Término")
                }
            }
        }
    }
}

@Composable
fun CardInputItem(
    card: Card,
    onUpdate: (Card) -> Unit,
    onRemove: () -> Unit
) {
    var term by remember { mutableStateOf(card.term) }
    var definition by remember { mutableStateOf(card.definition) }
    var imageUri by remember { mutableStateOf<Uri?>(card.imageUri?.let { Uri.parse(it) }) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            onUpdate(card.copy(imageUri = uri.toString()))
        }
    }

    // Tarjeta individual dentro del formulario
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // Agregamos un borde gris muy suave para que se distinga del fondo blanco
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(6.dp), // Un poco más de sombra
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red)
                }
            }

            OutlinedTextField(
                value = term,
                onValueChange = {
                    term = it
                    onUpdate(card.copy(term = it))
                },
                label = { Text("Término") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = definition,
                onValueChange = {
                    definition = it
                    onUpdate(card.copy(definition = it))
                },
                label = { Text("Definición / Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { launcher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3E4FA), contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Image, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imagen")
                }

                if (imageUri != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}