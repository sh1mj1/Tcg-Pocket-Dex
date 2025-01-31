package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.fakeCardDetail
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.tierdecks.fakeDecksInformation
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

val tempCardDetail = fakeCardDetail

// TODO: constructor param viewmodel
@Composable
fun CardDetailScreen(
    cardId: String? = "",
    deckItemsState: List<DeckItemState>,
    onExpandDeck: (DeckItemState, Boolean) -> Unit,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: fetch the data from viewmodel
    val cardDetail = tempCardDetail
    val relatedCards = fakeCardsData.subList(0, 5)
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        item {
            CardDetailMetaDataSection(
                cardDetail = cardDetail,
                modifier = modifier,
            )

            Spacer(modifier = Modifier.height(12.dp))

            PokemonMovesSection(
                pokemonMoves = cardDetail.pokemonMoves,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            RelatedCardsSection(
                relatedCards = relatedCards,
                modifier = modifier,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            RelatedDecksSection(
                deckItemsState = deckItemsState,
                onExpandDeck = onExpandDeck,
                onDeckItemClick = onDeckItemClick,
                modifier = modifier,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardDetailScreenPreview() {
    TcgPocketDexTheme {
        CardDetailScreen(
            cardId = "1",
            deckItemsState = fakeDecksInformation.map(::DeckItemState),
            onDeckItemClick = { },
            onExpandDeck = { deckItemStata, _ -> },
        )
    }
}

data class Energy(
    val type: String,
    val typeUrl: String,
)

private const val TAG = "CardDetailScreen"
