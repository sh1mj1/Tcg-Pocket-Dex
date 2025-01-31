package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.tierdecks.fakeCardDetail
import tcg.pocket.dex.tierdecks.temporalPokemonCardRarityPlaceholderDrawable
import tcg.pocket.dex.tierdecks.temporalPokemonTypePlaceholderDrawable
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun CardAttribute(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
    ) {
        Column {
            PokemonCardRarity(cardDetail)
            PokemonType(cardDetail)
            PokemonWeakness(cardDetail)
            PokemonHp(cardDetail)
            PokemonRetreatCost(cardDetail)
            PokemonEvolutionStage(cardDetail)
        }
    }
}

@Composable
private fun PokemonCardRarity(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Rarity:",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        AsyncImage(
            model = cardDetail.rarityUrl,
            contentDescription = "Rarity",
            placeholder =
                painterResource(
                    temporalPokemonCardRarityPlaceholderDrawable,
                ),
        )
    }
}

@Composable
private fun PokemonType(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Type:",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = cardDetail.type,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = cardDetail.typeImageUrl,
            contentDescription = "Type",
            placeholder = painterResource(temporalPokemonTypePlaceholderDrawable),
        )
    }
}

@Composable
private fun PokemonWeakness(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Weakness:",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        AsyncImage(
            model = cardDetail.weaknessTypeUrl,
            contentDescription = "Weakness",
            placeholder = painterResource(temporalPokemonTypePlaceholderDrawable),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = cardDetail.weakness,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PokemonHp(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "HP:",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = cardDetail.hp,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PokemonRetreatCost(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Retreat Cost:",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = cardDetail.retreatCost.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PokemonEvolutionStage(cardDetail: CardDetail) {
    Row(
        modifier =
            Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Evolution Stage:",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = cardDetail.stage.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardAttributePreview() {
    TcgPocketDexTheme {
        CardAttribute(
            cardDetail = fakeCardDetail,
        )
    }
}
