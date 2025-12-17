package tcg.pocket.dex.repo.decks

import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.repo.tournamentstats.TournamentStatsRepo
import tcg.pocket.dex.tierdecks.DeckDetailInformation
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckSimpleInformation

class DefaultDecksRepo(
    private val tournamentStatsRepo: TournamentStatsRepo,
) : DecksRepo {
    override suspend fun allTierDecks(): List<DeckInformation> {
        return tournamentStatsRepo.getDeckStatistics()
            .sortedByDescending { it.appearances }
            .mapIndexed { index, calculatedDeck ->
                calculatedDeck.toDeckInformation(rank = index + 1)
            }
    }
}

private fun CalculatedDeck.toDeckInformation(rank: Int): DeckInformation {
    val pokemonNames = deckId.split("|")
    return DeckInformation(
        simple =
            DeckSimpleInformation(
                deckId = deckId,
                // TODO: Replace with actual Pokémon image URLs from card data (Issue #36)
                representativePokemonImageUrls =
                    pokemonNames.take(2).map {
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
                    },
                rank = rank,
                deckName = deckName,
                winRate = winRate,
                share = usageShare,
            ),
        detail =
            DeckDetailInformation(
                // TODO: Calculate actual deck cost from card data (Issue #36)
                cost = 0,
                // TODO: Extract Pokémon types from card data (Issue #36)
                pokemonTypes = emptyList(),
                // TODO: Generate deck description or fetch from backend (Issue #36)
                description = "",
            ),
    )
}
