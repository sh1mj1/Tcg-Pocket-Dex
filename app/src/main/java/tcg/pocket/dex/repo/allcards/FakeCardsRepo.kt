package tcg.pocket.dex.repo.allcards

import tcg.pocket.dex.allcards.CardData

class FakeCardsRepo(
    private val fakeCards: List<CardData> = fakeCardsData,
) : CardsRepo {
    override fun allCards(): List<CardData> = fakeCards

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
    }
}
