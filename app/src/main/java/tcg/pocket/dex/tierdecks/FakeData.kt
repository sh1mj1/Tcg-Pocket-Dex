package tcg.pocket.dex.tierdecks

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import tcg.pocket.dex.R
import tcg.pocket.dex.allcards.CardData

val temporalPokemonPlaceholderDrawable: Int = R.drawable.tcg_pocket_unknown
val temporalPokemonCardPlaceholderDrawable: Int = R.drawable.pocket_dex_card_image
val temporalPokemonCardRarityPlaceholderDrawable: Int = R.drawable.pocket_dex_rarity
val temporalPokemonTypePlaceholderDrawable: Int = R.drawable.pocket_dex_type_image

val fakeFightingTypeChipData =
    PokemonTypeChipData(
        imageUrl = "https://static.dotgg.gg/pokepocket/icons/fighting.png",
        count = 1,
    )
val fakeFirePokemonTypeChipData =
    PokemonTypeChipData(
        imageUrl = "https://static.dotgg.gg/pokepocket/icons/fire.png",
        count = 1,
    )
val fakeWaterPokemonTypeChipData =
    PokemonTypeChipData(
        imageUrl = "https://static.dotgg.gg/pokepocket/icons/water.png",
        count = 1,
    )

val fakePokemonTypeChipDataset =
    listOf(
        fakeFightingTypeChipData,
        fakeFirePokemonTypeChipData,
        fakeWaterPokemonTypeChipData,
    )

val fakeTierDeckDescription =
    "The Mewtwo ex Gardevoir deck relies heavily on Mewtwo ex (A1) as the main damage dealer, " +
        "using its early Psychic Sphere as an early damage dealer " +
        "to threaten to knock out most Pokemon with two attacks. " +
        "However, Mewtwo ex (A1)'s Psydrive is where things start kicking off, " +
        "allowing you to damage the opponent's Pokemon by 150, " +
        "knocking out almost any Pokemon with 1 hit."

fun fakeSimpleUrl(dexNumber: Int): String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$dexNumber.png"

val fakeDeckInformation =
    DeckInformation(
        simple =
            DeckSimpleInformation(
                deckId = "deckId",
                representativePokemonImageUrls =
                    listOf(
                        fakeSimpleUrl(150),
                        fakeSimpleUrl(282),
                    ),
                rank = 1,
                deckName = "Deck Name",
                winRate = "53.64%",
                share = "17.52%",
            ),
        detail =
            DeckDetailInformation(
                cost = 100,
//        pokemonTypes = fakePokemonTypeChipDataset,
                pokemonTypes = listOf(fakeWaterPokemonTypeChipData),
                description = fakeTierDeckDescription,
            ),
    )

val fakeTypesUrl =
    listOf(
        "https://static.dotgg.gg/pokepocket/icons/fire.png",
        "https://static.dotgg.gg/pokepocket/icons/water.png",
        "https://static.dotgg.gg/pokepocket/icons/grass.png",
        "https://static.dotgg.gg/pokepocket/icons/dragon.png",
        "https://static.dotgg.gg/pokepocket/icons/fighting.png",
    )

val fakeDecksInformation =
    List(15) { index ->
        DeckInformation(
            simple =
                DeckSimpleInformation(
                    deckId = "$index",
                    representativePokemonImageUrls =
                        listOf(
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${index + 1}.png",
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${index + 2}.png",
                        ),
                    rank = index + 1,
                    deckName = "Deck Name $index",
                    winRate = "${50 + index % 10}.${index % 10}%",
                    share = "${10 + index % 5}.${index % 5}%",
                ),
            detail =
                DeckDetailInformation(
                    cost = 3 + index % 5,
                    pokemonTypes =
                        listOf(
                            PokemonTypeChipData(
                                imageUrl = fakeTypesUrl.random(),
                                count = index % 3 + 1,
                            ),
                            PokemonTypeChipData(
                                imageUrl = fakeTypesUrl.random(),
                                count = index % 2 + 1,
                            ),
                        ),
                    description = LoremIpsum(20).values.joinToString(),
                ),
        )
    }

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
