package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tcg.pocket.dex.common.UiState
import tcg.pocket.dex.component.DeckList
import tcg.pocket.dex.repo.decks.FakeDecksRepo
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun TierDecksScreen(
    viewModel: TierDecksViewModel,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        is UiState.Success -> {
            LazyColumn(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(8.dp),
            ) {
                item {
                    DeckList(
                        deckItemsState = state.data,
                        onExpandDeck = viewModel::onExpandDeck,
                        onDeckItemClick = onDeckItemClick,
                    )
                }
            }
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
