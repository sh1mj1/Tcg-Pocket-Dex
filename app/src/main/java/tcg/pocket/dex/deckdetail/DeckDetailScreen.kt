package tcg.pocket.dex.deckdetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeckDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DeckDetailViewModel,
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text =
                """
                Deck Detail 
                id: ${viewModel.deckId}
                """.trimIndent(),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}
