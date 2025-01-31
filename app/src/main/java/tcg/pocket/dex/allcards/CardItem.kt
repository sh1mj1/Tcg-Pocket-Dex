package tcg.pocket.dex.allcards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
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
fun CardItem(
    card: CardData,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable(onClick = {
                    onClick(card.Id)
                }),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = card.imageUrl,
                contentDescription = card.name,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(temporalPokemonCardPlaceholderDrawable),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = card.name,
                style =
                    MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
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

@Preview
@Composable
private fun CardItemPreview() {
    TcgPocketDexTheme {
        CardItem(
            card = fakeCardsData[0],
            onClick = {},
        )
    }
}
