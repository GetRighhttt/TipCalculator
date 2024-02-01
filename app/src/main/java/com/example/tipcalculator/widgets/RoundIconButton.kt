package com.example.tipcalculator.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)

@Composable
inline fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    crossinline onClick: () -> Unit,
    tint: Color = Color(0xFFFE9D7D),
    backgroundColor: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
    ElevatedCard(modifier = modifier
        .padding(4.dp)
        .clickable { onClick.invoke() } then IconButtonSizeModifier, // concatenates modifiers
        shape = CircleShape,
        colors = backgroundColor,
        elevation = elevation
    ) {
        Icon(
            imageVector,
            contentDescription = "Plus or Minus",
            modifier = IconButtonSizeModifier,
            tint = tint
        )
    }
}