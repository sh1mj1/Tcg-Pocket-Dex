package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tcg.pocket.dex.component.DeckList
import tcg.pocket.dex.repo.decks.FakeDecksRepo
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun TierDecksScreen(
    viewModel: TierDecksViewModel,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val deckItemsState by viewModel.deckItemsState.collectAsStateWithLifecycle()
    LazyColumn(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
    ) {
        item {
            DeckList(
                deckItemsState = deckItemsState,
                onExpandDeck = viewModel::onExpandDeck,
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
            viewModel = TierDecksViewModel(decksRepo = FakeDecksRepo()),
            onDeckItemClick = { },
        )
    }
}
