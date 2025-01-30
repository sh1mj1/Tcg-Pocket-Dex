package tcg.pocket.dex.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun SearchScreenForTierDecks(modifier: Modifier = Modifier) {
    // TODO: Implement search screen
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Search Screen For Tier Decks",
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Composable
fun SearchScreenForAllCards(modifier: Modifier = Modifier) {
    // TODO: Implement search screen
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Search Screen For All Cards",
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Composable
fun SearchScreenForExpansionPacks(modifier: Modifier = Modifier) {
    // TODO: Implement search screen
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Search Screen For Expansion Packs",
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Preview
@Composable
private fun SearchScreenForTierDecksPreview() {
    TcgPocketDexTheme {
        SearchScreenForTierDecks()
    }
}
