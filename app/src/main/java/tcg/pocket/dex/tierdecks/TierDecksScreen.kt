package tcg.pocket.dex.tierdecks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

// TODO: viewModel
val deckItemsState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.map { DeckItemState(it) })
    }

@Composable
fun TierDecksScreen() {
    DeckList(
        deckItemsState = deckItemsState,
        onExpandDeck = { deckItemState, expanded ->
            deckItemState.toggleExpanded()
        },
        onDeckItemClick = {},
    )
}

@Preview
@Composable
private fun TireDecksScreenPreview() {
    TcgPocketDexTheme {
        TierDecksScreen()
    }
}
