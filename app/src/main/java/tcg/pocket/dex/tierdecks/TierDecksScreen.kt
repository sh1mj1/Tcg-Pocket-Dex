package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.component.DeckList
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun TierDecksScreen(
    deckItemsState: List<DeckItemState>,
    onExpandDeck: (DeckItemState, Boolean) -> Unit,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
    ) {
        item {
            DeckList(
                deckItemsState = deckItemsState,
                onExpandDeck = onExpandDeck,
                onDeckItemClick = onDeckItemClick,
            )
        }
    }
}

@Preview
@Composable
private fun TierDeckScreenPreview() {
    TcgPocketDexTheme {
        TierDecksScreen(
            deckItemsState = fakeDecksInformation.map(::DeckItemState),
            onDeckItemClick = { },
            onExpandDeck = { deckItemStata, _ -> },
        )
    }
}
