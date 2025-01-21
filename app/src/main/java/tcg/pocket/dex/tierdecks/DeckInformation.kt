package tcg.pocket.dex.tierdecks

data class DeckInformation(
    val simple: DeckSimpleInformation,
    val detail: DeckDetailInformation,
)

data class DeckSimpleInformation(
    val deckId: String,
    val representativePokemonImageUrls: List<String>,
    val rank: Int,
    val deckName: String,
    val winRate: String,
    val share: String,
)

data class DeckDetailInformation(
    val cost: Int,
    val pokemonTypes: List<PokemonTypeChipData>,
    val description: String,
)
