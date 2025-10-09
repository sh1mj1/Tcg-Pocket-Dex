package tcg.pocket.dex.repo.allcards

import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.allcards.MoveEnergy
import tcg.pocket.dex.allcards.PokemonMove

class FakeCardsRepo(
    private val cards: List<CardData> = fakeCardsData,
    private val cardDetail: CardDetail = fakeCardDetail,
    private val relatedCards: List<CardData> = fakeRelatedCards,
) : CardsRepo {
    override suspend fun allCards(): List<CardData> = cards

    override suspend fun cardDetail(id: String): CardDetail = cardDetail

    override fun relatedCards(id: String): List<CardData> = relatedCards

    companion object {
        val fakeCardsData =
            listOf(
                CardData(
                    name = "Bulbasaur",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000010_00_FUSHIGIDANE_C.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_C.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Grass.png?width=40&height=40",
                ),
                CardData(
                    name = "Ivysaur",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000020_00_FUSHIGISOU_U.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_U.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Grass.png?width=40&height=40",
                ),
                CardData(
                    name = "Venusaur",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000030_00_FUSHIGIBANA_R.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_R.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Grass.png?width=40&height=40",
                ),
                CardData(
                    name = "Venusaur ex",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000040_00_FUSHIGIBANAex_RR.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_RR.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Grass.png?width=40&height=40",
                ),
                CardData(
                    name = "Arcanine",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000400_00_WINDIE_R.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_R.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Fire.png?width=40&height=40",
                ),
                CardData(
                    name = "Arcanine ex",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000410_00_WINDIEex_RR.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_RR.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Fire.png?width=40&height=40",
                ),
                CardData(
                    name = "Psyduck",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000570_00_KODUCK_C.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_C.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Water.png?width=40&height=40",
                ),
                CardData(
                    name = "Golduck",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000580_00_GOLDUCK_U.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_U.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Water.png?width=40&height=40",
                ),
                CardData(
                    name = "Pikachu",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000940_00_PIKACHU_C.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_C.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Lightning.png?width=40&height=40",
                ),
                CardData(
                    name = "Raichu",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000950_00_RAICHU_R.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_R.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Lightning.png?width=40&height=40",
                ),
                CardData(
                    name = "Pikachu ex",
                    imageUrl =
                        "https://assets.pokemon-zone.com/game-assets/" +
                            "CardPreviews/cPK_10_000960_00_PIKACHUex_RR.webp",
                    rarityUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/RarityIcon_RR.png?height=40",
                    typeUrl = "https://static.mana.wiki/tcgwiki-pokemonpocket/TypeIcon_Lightning.png?width=40&height=40",
                ),
            )

        val fakeCardDetail =
            CardDetail(
                category = "Pokemon",
                name = "Venusaur ex",
                rarity = "Double Rare",
                rarityIcon = tcg.pocket.dex.R.drawable.double_rare_or_ultra_rare,
                type = "Grass",
                typeIcon = tcg.pocket.dex.R.drawable.grass_icon,
                weakness = "+20",
                weaknessType = "Fire",
                weaknessTypeIcon = tcg.pocket.dex.R.drawable.fire_icon,
                hp = "150",
                retreatCost = 3,
                stage = 1,
                description = "Psychic Sphere: 50\nPsydrive: 150",
                imageUrl =
                    "https://assets.pokemon-zone.com/game-assets/CardPreviews/" +
                        "cPK_10_000040_00_FUSHIGIBANAex_RR.webp",
                pokemonMoves =
                    listOf(
                        PokemonMove(
                            name = "Razor Leaf",
                            damage = "50",
                            energy =
                                listOf(
                                    MoveEnergy(
                                        type = "Grass",
                                        typeIcon = tcg.pocket.dex.R.drawable.grass_icon,
                                    ),
                                    MoveEnergy(
                                        type = "Normal",
                                        typeIcon = tcg.pocket.dex.R.drawable.pocket_dex_type_image,
                                    ),
                                    MoveEnergy(
                                        type = "Normal",
                                        typeIcon = tcg.pocket.dex.R.drawable.pocket_dex_type_image,
                                    ),
                                ),
                            description = "",
                        ),
                        PokemonMove(
                            name = "Giant Bloom",
                            damage = "100",
                            energy =
                                listOf(
                                    MoveEnergy(
                                        type = "Grass",
                                        typeIcon = tcg.pocket.dex.R.drawable.grass_icon,
                                    ),
                                    MoveEnergy(
                                        type = "Grass",
                                        typeIcon = tcg.pocket.dex.R.drawable.grass_icon,
                                    ),
                                    MoveEnergy(
                                        type = "Normal",
                                        typeIcon = tcg.pocket.dex.R.drawable.pocket_dex_type_image,
                                    ),
                                    MoveEnergy(
                                        type = "Normal",
                                        typeIcon = tcg.pocket.dex.R.drawable.pocket_dex_type_image,
                                    ),
                                ),
                            description = "Heal 30 damage from this Pokémon.",
                        ),
                    ),
                trainerType = null,
                effect = null,
            )

        val fakeTrainerCardDetail =
            CardDetail(
                category = "Trainer",
                name = "Professor's Research",
                rarity = "Uncommon",
                rarityIcon = tcg.pocket.dex.R.drawable.uncommon,
                type = "",
                typeIcon = 0,
                weakness = "",
                weaknessType = "",
                weaknessTypeIcon = 0,
                hp = "",
                retreatCost = 0,
                stage = 0,
                description = "",
                effect = "Discard your hand and draw 7 cards.",
                imageUrl = "https://assets.tcgdex.net/en/sv/sv1/189/",
                pokemonMoves = emptyList(),
                trainerType = "Supporter",
            )

        val fakeRelatedCards = fakeCardsData.subList(fromIndex = 0, toIndex = 5)
    }
}
