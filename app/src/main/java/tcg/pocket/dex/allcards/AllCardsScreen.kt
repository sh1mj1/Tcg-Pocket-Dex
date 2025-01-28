package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AllCardsScreen(modifier: Modifier = Modifier) {
    // TODO: Implement search screen
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "All Cards",
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}
