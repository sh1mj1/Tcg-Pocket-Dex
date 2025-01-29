package tcg.pocket.dex.deckdetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeckDetailScreen(
    deckId: String?,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text =
                """
                Deck Detail 
                id: $deckId
                """.trimIndent(),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Preview
@Composable
private fun DeckDetailScreenPreview() {
    DeckDetailScreen(deckId = "FakeId")
}
