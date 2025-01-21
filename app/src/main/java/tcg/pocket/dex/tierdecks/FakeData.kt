package tcg.pocket.dex.tierdecks

import tcg.pocket.dex.R

val temporalPokemonPlaceholderDrawable: Int = R.drawable.tcg_pocket_unknown

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
