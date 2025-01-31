package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.component.PocketDexSectionHeader
import tcg.pocket.dex.tierdecks.DeckItem
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.fakeDecksInformation
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun RelatedDecksSection(
    deckItemsState: List<DeckItemState>,
    onExpandDeck: (DeckItemState, Boolean) -> Unit,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        PocketDexSectionHeader(
            text = "Related Decks",
            modifier = modifier,
        )
        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
        ) {
            Column {
                deckItemsState.forEach { deckItemState ->
                    DeckItem(
                        information = deckItemState.content,
                        expanded = deckItemState.expanded,
                        onExpandedChange = { expanded ->
                            onExpandDeck(deckItemState, expanded)
                        },
                        onCardClick = onDeckItemClick,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RelatedDecksContentPreview() {
    TcgPocketDexTheme {
        RelatedDecksSection(
            deckItemsState = fakeDecksInformation.map(::DeckItemState),
            onDeckItemClick = { },
            onExpandDeck = { deckItemStata, _ -> },
        )
    }
}
