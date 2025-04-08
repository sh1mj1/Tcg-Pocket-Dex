package tcg.pocket.dex.repo.decks

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import tcg.pocket.dex.tierdecks.DeckDetailInformation
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckSimpleInformation
import tcg.pocket.dex.tierdecks.PokemonTypeChipData
import tcg.pocket.dex.tierdecks.fakeTypesUrl

class FakeDecksRepo(
    private val tierDecks: List<DeckInformation> = fakeTierDecksInformation,
) : DecksRepo {
    override fun allTierDecks(): List<DeckInformation> = tierDecks

    companion object {
        val fakeTierDecksInformation =
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
    }
}
