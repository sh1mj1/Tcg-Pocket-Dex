package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import tcg.pocket.dex.ui.theme.ChipSize
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun PokemonTypeChipGroup(
    chips: List<PokemonTypeChipData>,
    modifier: Modifier = Modifier,
    chipSize: Dp = ChipSize.Medium,
) {
    Row(
        modifier =
            modifier
                .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        chips.forEach { chipData ->
            PokemonTypeChip(
                pokemonTypeChipData = chipData,
                chipSize = chipSize,
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun PokemonTypeChipGroupPreview() {
    TcgPocketDexTheme {
        PokemonTypeChipGroup(
            chips = fakePokemonTypeChipDataset,
        )
    }
}
