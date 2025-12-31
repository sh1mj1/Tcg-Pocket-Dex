package tcg.pocket.dex.repo.tournamentstats

import tcg.pocket.dex.CalculatedDeck

interface TournamentStatsRepo {
    suspend fun getDeckStatistics(): List<CalculatedDeck>

    suspend fun getDeckById(deckId: String): CalculatedDeck?
}
