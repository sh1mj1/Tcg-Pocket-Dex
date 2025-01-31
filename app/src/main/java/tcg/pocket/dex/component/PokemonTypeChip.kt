package tcg.pocket.dex.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.tierdecks.PokemonTypeChipData
import tcg.pocket.dex.tierdecks.fakeFirePokemonTypeChipData
import tcg.pocket.dex.tierdecks.temporalPokemonPlaceholderDrawable
import tcg.pocket.dex.ui.theme.ChipSize
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun PokemonTypeChip(
    pokemonTypeChipData: PokemonTypeChipData,
    modifier: Modifier = Modifier,
    chipSize: Dp = ChipSize.Medium,
) {
    Card(
        modifier =
            modifier
                .padding(4.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .padding(4.dp)
                    .wrapContentWidth(),
        ) {
            AsyncImage(
                model = pokemonTypeChipData.imageUrl,
                contentDescription = "Pokemon type icon",
                placeholder = painterResource(temporalPokemonPlaceholderDrawable),
                modifier =
                    Modifier
                        .size(chipSize)
                        .padding(4.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = pokemonTypeChipData.count.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun PokemonTypeChipPreview() {
    TcgPocketDexTheme {
        PokemonTypeChip(
            pokemonTypeChipData = fakeFirePokemonTypeChipData,
        )
    }
}
