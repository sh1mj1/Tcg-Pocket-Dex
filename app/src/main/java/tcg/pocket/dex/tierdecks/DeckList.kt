package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun DeckList(
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
        items(
            items = deckItemsState,
            key = { deckItem -> deckItem.content.simple.deckId },
        ) { deckItemState ->

            DeckItem(
                information = deckItemState.content,
                expanded = deckItemState.expanded,
                onExpandedChange = { expanded -> onExpandDeck(deckItemState, expanded) },
                onCardClick = onDeckItemClick,
            )
        }
    }
}

@Preview
@Composable
private fun DeckListPreview() {
    TcgPocketDexTheme {
        DeckList(
            deckItemsState = fakeDecksInformation.map(::DeckItemState),
            onDeckItemClick = { },
            onExpandDeck = { deckItemStata, _ -> },
        )
    }
}
