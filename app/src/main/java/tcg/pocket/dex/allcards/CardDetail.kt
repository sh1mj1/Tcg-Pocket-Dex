package tcg.pocket.dex.allcards

import androidx.annotation.DrawableRes

data class CardDetail(
    val category: String = "",
    val name: String,
    val rarity: String,
    @DrawableRes val rarityIcon: Int,
    val type: String,
    @DrawableRes val typeIcon: Int,
    val weakness: String,
    val weaknessType: String,
    @DrawableRes val weaknessTypeIcon: Int,
    val hp: String,
    val retreatCost: Int,
    val stage: Int,
    val description: String,
    val imageUrl: String,
    val pokemonMoves: List<PokemonMove>,
    val trainerType: String? = null,
    val effect: String? = null,
)
