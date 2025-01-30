package tcg.pocket.dex.allcards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.tierdecks.temporalPokemonCardPlaceholderDrawable
import tcg.pocket.dex.tierdecks.temporalPokemonCardRarityPlaceholderDrawable
import tcg.pocket.dex.tierdecks.temporalPokemonTypePlaceholderDrawable
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun AllCardsScreen(
    modifier: Modifier = Modifier,
    cards: List<CardData>,
    onCardClick: (CardData) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier =
            modifier
                .fillMaxSize()
                .padding(8.dp),
    ) {
        items(cards.size) { index ->
            val card = cards[index]
            CardItem(card, onClick = { onCardClick(card) })
        }
    }
}

@Composable
fun CardItem(
    card: CardData,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = card.imageUrl,
                contentDescription = card.name,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(temporalPokemonCardPlaceholderDrawable),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = card.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AsyncImage(
                    model = card.rarityUrl,
                    contentDescription = "Rarity",
                    placeholder = painterResource(temporalPokemonCardRarityPlaceholderDrawable),
                )
                AsyncImage(
                    model = card.typeUrl,
                    contentDescription = "Type",
                    placeholder = painterResource(temporalPokemonTypePlaceholderDrawable),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TierDecksScreenPreview() {
    TcgPocketDexTheme {
        AllCardsScreen(
            cards = fakeCardsData,
            onCardClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardItemPreview() {
    TcgPocketDexTheme {
        CardItem(
            card = fakeCardsData[0],
            onClick = {},
        )
    }
}

data class CardData(
    val name: String,
    val imageUrl: String,
    val rarityUrl: String,
    val typeUrl: String,
)
