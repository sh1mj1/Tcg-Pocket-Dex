package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.component.PocketDexSectionHeader
import tcg.pocket.dex.tierdecks.fakeCardDetail
import tcg.pocket.dex.tierdecks.temporalPokemonTypePlaceholderDrawable
import tcg.pocket.dex.ui.theme.ChipSize
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun PokemonMoves(
    pokemonMoves: List<PokemonMove>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PocketDexSectionHeader(
            text = "Moves",
        )

        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                pokemonMoves.forEach { move ->
                    Column(
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Row {
                            move.energy.forEach { energy ->
                                AsyncImage(
                                    model = energy.typeUrl,
                                    contentDescription = energy.type,
                                    modifier = Modifier.size(ChipSize.Small),
                                    placeholder =
                                    painterResource(
                                        temporalPokemonTypePlaceholderDrawable,
                                    ),
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = move.name,
                            )
                            Text(
                                text = move.damage.toString(),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End,
                            )
                        }

                        if (move.hasDescription) {
                            Text(
                                text = move.description,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonMovesPreview() {
    TcgPocketDexTheme {
        PokemonMoves(
            pokemonMoves = fakeCardDetail.pokemonMoves,
        )
    }
}

data class PokemonMove(
    val name: String,
    val damage: Int,
    val energy: List<Energy>,
    val description: String,
) {
    val hasDescription: Boolean
        get() = description.isNotEmpty()
}
