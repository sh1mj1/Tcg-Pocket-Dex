package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tcg.pocket.dex.repo.allcards.FakeCardsRepo
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun CardDetailScreen(
    viewModel: CardDetailViewModel,
    onExpandDeck: (DeckItemState) -> Unit,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardDetail by viewModel.cardDetailState.collectAsStateWithLifecycle()
    val relatedCards by viewModel.relatedCardsState.collectAsStateWithLifecycle()
    val relatedDecks by viewModel.relatedDecksState.collectAsStateWithLifecycle()

    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        cardDetail?.let { cardDetail ->
            LazyColumn(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(16.dp),
            ) {
                item {
                    CardDetailMetaDataSection(
                        cardDetail = cardDetail,
                        modifier = Modifier.fillMaxHeight(),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PokemonMovesSection(
                        pokemonMoves = cardDetail.pokemonMoves,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

//                RelatedCardsSection(
//                    relatedCards = relatedCards,
//                    modifier = modifier,
//                )
//                Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    RelatedDecksSection(
                        deckItemsState = relatedDecks,
                        onExpandDeck = onExpandDeck,
                        onDeckItemClick = onDeckItemClick,
                        modifier = modifier,
                    )
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardDetailScreenPreview() {
    TcgPocketDexTheme {
        CardDetailScreen(
            viewModel = CardDetailViewModel(cardId = "", cardsRepo = FakeCardsRepo()),
            onDeckItemClick = { },
            onExpandDeck = { deckItemState -> },
        )
    }
}

private const val TAG = "CardDetailScreen"
