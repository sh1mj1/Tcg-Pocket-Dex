package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.component.DeckList
import tcg.pocket.dex.component.PocketDexSectionHeader
import tcg.pocket.dex.repo.decks.FakeDecksRepo
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun RelatedDecksSection(
    deckItemsState: List<DeckItemState>,
    onExpandDeck: (DeckItemState) -> Unit,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        PocketDexSectionHeader(
            text = "Related Decks",
            modifier = modifier,
        )
        DeckList(deckItemsState, onExpandDeck, onDeckItemClick)
    }
}

@Preview(showBackground = true)
@Composable
private fun RelatedDecksContentPreview() {
    TcgPocketDexTheme {
        RelatedDecksSection(
            deckItemsState =
                FakeDecksRepo.fakeTierDecksInformation.subList(0, 5)
                    .map(::DeckItemState),
            onDeckItemClick = { },
            onExpandDeck = { deckItemState -> },
        )
    }
}
