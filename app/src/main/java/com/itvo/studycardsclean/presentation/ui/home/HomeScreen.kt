package com.itvo.studycardsclean.presentation.ui.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itvo.studycardsclean.domain.model.CardSearchResult
import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.presentation.viewmodel.HomeViewModel
import com.itvo.studycardsclean.presentation.ui.components.SubjectItemCard
import com.itvo.studycardsclean.presentation.ui.components.bounceClick

val pastelColors = listOf("#FFD1DC", "#A2CFFE", "#AAF0D1", "#E3E4FA", "#FFFACD", "#FFDAB9", "#DCD0FF", "#B0E0E6")

@Composable
fun HomeScreen(
    // Hilt se encarga de buscarlo o crearlo automáticamente
    viewModel: HomeViewModel = hiltViewModel(),
    onSubjectClick: (Int) -> Unit,
    onSearchResultClick: (Int) -> Unit = { }
) {
    val subjectsList by viewModel.subjectsList.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    // Estados
    var showDialog by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var subjectToEdit by remember { mutableStateOf<Subject?>(null) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            if (isSearching) {
                GoogleStyleSearchBar(
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    onClose = {
                        isSearching = false
                        viewModel.onSearchQueryChange("")
                    }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                isSearching = isSearching,
                onHomeClick = {
                    isSearching = false
                    viewModel.onSearchQueryChange("")
                },
                onAddClick = {
                    subjectToEdit = null
                    showDialog = true
                },
                onSearchClick = { isSearching = true }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // --- MODO 1: LISTA NORMAL DE MATERIAS ---
                if (!isSearching) {
                    HomeHeader()

                    if (subjectsList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No tienes materias aún", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                            items(subjectsList) { subject ->
                                SubjectItemCard(
                                    subject = subject,
                                    onClick = { onSubjectClick(subject.id) },
                                    onDeleteClick = { viewModel.deleteSubject(subject) },
                                    onEditClick = {
                                        subjectToEdit = subject
                                        showDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
                // --- MODO 2: RESULTADOS DE BÚSQUEDA ---
                else {
                    Spacer(modifier = Modifier.height(80.dp)) // Espacio para la barra flotante

                    if (searchResults.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Escribe para buscar en tus tarjetas...", color = Color.Gray)
                        }
                    } else {
                        Text(
                            "Resultados encontrados:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(searchResults) { result ->
                                SearchResultItem(
                                    result = result,
                                    onClick = { onSearchResultClick(result.topicId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddSubjectDialog(
            subjectToEdit = subjectToEdit,
            onDismiss = { showDialog = false },
            onConfirm = { name, color, imageUri ->
                val idToSave = subjectToEdit?.id ?: 0
                viewModel.saveSubject(id = idToSave, name = name, color = color, imageUri = imageUri)
                showDialog = false
            }
        )
    }
}

// --- NUEVO COMPONENTE: TARJETA DE RESULTADO DE BÚSQUEDA ---
@Composable
fun SearchResultItem(result: CardSearchResult, onClick: () -> Unit) {
    val badgeColor = try {
        Color(android.graphics.Color.parseColor(result.subjectColor))
    } catch (e: Exception) { Color.Gray }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), // Gris muy clarito
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila superior: Dónde se encontró (Materia > Tema)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${result.subjectName} > ${result.topicName}",
                    style = TextStyle(fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Término encontrado
            Text(
                text = result.term,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            )

            // Descripción cortada (si es muy larga)
            Text(
                text = result.definition,
                style = TextStyle(fontSize = 14.sp, color = Color.DarkGray),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ... (El resto de tus componentes: GoogleStyleSearchBar, BottomNavigationBar, AddSubjectDialog, HomeHeader, etc. déjalos igual) ...
// SOLO ASEGÚRATE DE COPIARLOS SI BORRASTE EL ARCHIVO COMPLETO
// Aquí abajo pongo los esenciales para que compile si copiaste todo:

@Composable
fun GoogleStyleSearchBar(onQueryChange: (String) -> Unit, onClose: () -> Unit) {
    var text by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.statusBarsPadding().fillMaxWidth().padding(top = 12.dp, start = 16.dp, end = 16.dp).height(56.dp),
        shape = RoundedCornerShape(28.dp), shadowElevation = 6.dp, color = Color.White
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, "Buscar", tint = Color.Gray)
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it; onQueryChange(it) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                decorationBox = { inner -> if (text.isEmpty()) Text("Buscar palabra...", color = Color.Gray, fontSize = 18.sp); inner() }
            )
            IconButton(onClick = onClose) { Icon(Icons.Default.Close, "Cerrar", tint = Color.Gray) }
        }
    }
}

@Composable
fun BottomNavigationBar(isSearching: Boolean, onHomeClick: () -> Unit, onAddClick: () -> Unit, onSearchClick: () -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 10.dp) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = !isSearching, onClick = onHomeClick)
        NavigationBarItem(icon = { Icon(Icons.Default.Add, null) }, label = { Text("Agregar") }, selected = false, onClick = onAddClick)
        NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, label = { Text("Buscar") }, selected = isSearching, onClick = onSearchClick)
    }
}

@Composable
fun AddSubjectDialog(subjectToEdit: Subject? = null, onDismiss: () -> Unit, onConfirm: (String, String, String?) -> Unit) {
    // ... (Tu código del diálogo que ya tenías)
    // Para no hacer el mensaje eterno, asumo que tienes este bloque. Si no, avísame.
    var name by remember { mutableStateOf(subjectToEdit?.name ?: "") }
    var selectedColor by remember { mutableStateOf(subjectToEdit?.colorHex ?: pastelColors[0]) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(subjectToEdit?.imageUri?.let { Uri.parse(it) }) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> if (uri != null) selectedImageUri = uri }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (subjectToEdit == null) "Nueva Materia" else "Editar Materia") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Text("Color:")
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    pastelColors.take(4).forEach { ColorCircle(it, selectedColor == it) { selectedColor = it } }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    pastelColors.takeLast(4).forEach { ColorCircle(it, selectedColor == it) { selectedColor = it } }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor(selectedColor)))) {
                    Text(if(selectedImageUri == null) "Subir Imagen" else "Cambiar Imagen", color = Color.Black)
                }
            }
        },
        confirmButton = { Button(onClick = { if (name.isNotEmpty()) onConfirm(name, selectedColor, selectedImageUri?.toString()) }) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun ColorCircle(colorHex: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(android.graphics.Color.parseColor(colorHex))).border(if (isSelected) 3.dp else 0.dp, Color.Black, CircleShape).clickable { onClick() })
}

@Composable
fun HomeHeader() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp)) {
        Text("Tu espacio de estudio", style = TextStyle(fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
            Text("Mis Materias", style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black))
            Spacer(modifier = Modifier.width(12.dp))
            Icon(imageVector = Icons.Default.AutoStories, contentDescription = null, tint = Color(0xFFDCD0FF), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.width(60.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFDCD0FF)))
    }
}