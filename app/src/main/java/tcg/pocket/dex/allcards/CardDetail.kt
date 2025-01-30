package tcg.pocket.dex.allcards

data class CardDetail(
    val name: String,
    val rarity: String,
    val rarityUrl: String,
    val type: String,
    val typeImageUrl: String,
    val weakness: String,
    val weaknessType: String,
    val weaknessTypeUrl: String,
    val hp: String,
    val retreatCost: Int,
    val stage: Int,
    val description: String,
    val imageUrl: String,
    val pokemonMoves: List<PokemonMove>,
)

data class DeckDetails(
    val name: String,
    val type: String,
    val imageUrl: String,
)
