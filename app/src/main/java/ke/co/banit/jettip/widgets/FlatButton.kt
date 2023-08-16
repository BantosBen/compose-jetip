package ke.co.banit.jettip.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)

@Composable
fun FlatIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClickAction: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    Card(
        modifier = modifier
            .padding(all = 4.dp)
            .clickable { onClickAction.invoke() }
            .then(IconButtonSizeModifier),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Plus or Minus icon",
                tint = tint,
            )
        }
    }
}