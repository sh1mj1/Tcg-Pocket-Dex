package tcg.pocket.dex.repo.decks

import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.repo.tournamentstats.TournamentStatsRepo
import tcg.pocket.dex.tierdecks.DeckDetailInformation
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckSimpleInformation
import timber.log.Timber

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
    Timber.d("Converting deck: $deckName, iconUrls: $iconUrls")
    return DeckInformation(
        simple =
            DeckSimpleInformation(
                deckId = deckId,
                representativePokemonImageUrls =
                    iconUrls.take(2).map { pokemonName ->
                        val normalizedName =
                            pokemonName
                                .lowercase()
                                .replace(" ex", "")
                                .replace(" v", "")
                                .trim()
                        val imageUrl = "https://r2.limitlesstcg.net/pokemon/gen9/$normalizedName.png"
                        Timber.d("Pokemon image URL: '$pokemonName' -> '$imageUrl'")
                        imageUrl
                    }.ifEmpty {
                        listOf("https://r2.limitlesstcg.net/pokemon/gen9/ditto.png")
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
