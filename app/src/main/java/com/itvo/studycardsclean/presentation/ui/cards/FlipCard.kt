package com.itvo.studycardsclean.presentation.ui.cards

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
// CORRECCIÓN 1: El import con alias fundamental
import com.itvo.studycardsclean.domain.model.Card as DataCard
import com.itvo.studycardsclean.presentation.ui.components.bounceClick

@Composable
fun FlipCard(
    cardData: DataCard, // CORRECCIÓN 2: Ahora sí detecta el tipo
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (isFlipped) 180f else 0f, animationSpec = tween(durationMillis = 400), label = "rotation")

    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp).animateContentSize()
            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
            .bounceClick { isFlipped = !isFlipped },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
            if (rotation <= 90f) {
                Text(text = cardData.term, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
            } else {
                Column(modifier = Modifier.fillMaxWidth().graphicsLayer { rotationY = 180f }, horizontalAlignment = Alignment.CenterHorizontally) {
                    if (cardData.imageUri != null) {
                        AsyncImage(model = cardData.imageUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp).padding(bottom = 16.dp), contentScale = ContentScale.Crop)
                    }
                    // CORRECCIÓN 3: definition ya debe reconocerse
                    Text(text = cardData.definition, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Justify)
                }
            }
        }
    }
}