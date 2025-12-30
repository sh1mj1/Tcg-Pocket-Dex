package tcg.pocket.dex.repo.decks

import tcg.pocket.dex.tierdecks.DeckDetailInformation
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckSimpleInformation
import tcg.pocket.dex.tierdecks.PokemonTypeChipData
import tcg.pocket.dex.tierdecks.fakeTypesUrl

class FakeDecksRepo(
    private val tierDecks: List<DeckInformation> = fakeTierDecksInformation,
) : DecksRepo {
    override suspend fun allTierDecks(): List<DeckInformation> = tierDecks

    companion object {
        private val popularPokemonIds =
            listOf(
                150,
                25,
                6,
                146,
                282,
                144,
                121,
                145,
                18,
                9,
                94,
                143,
                65,
                149,
                68,
            )

        val fakeTierDecksInformation =
            List(15) { index ->
                val pokemonId1 = popularPokemonIds.getOrElse(index) { index + 1 }
                val pokemonId2 = popularPokemonIds.getOrElse((index + 1) % popularPokemonIds.size) { index + 2 }
                DeckInformation(
                    simple =
                        DeckSimpleInformation(
                            deckId = "$index",
                            representativePokemonImageUrls =
                                listOf(
                                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId1.png",
                                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId2.png",
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
                            description = "",
                        ),
                )
            }
    }
}
