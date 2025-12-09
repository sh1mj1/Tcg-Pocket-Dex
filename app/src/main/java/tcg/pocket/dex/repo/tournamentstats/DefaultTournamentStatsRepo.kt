package tcg.pocket.dex.repo.tournamentstats

import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.DeckStatsAggregator
import tcg.pocket.dex.datasource.RemoteTournamentDataSource
import tcg.pocket.dex.toCalculatedDecks

class DefaultTournamentStatsRepo(
    private val remoteTournamentDataSource: RemoteTournamentDataSource,
) : TournamentStatsRepo {
    override suspend fun getDeckStatistics(): List<CalculatedDeck> {
        val tournamentIds = remoteTournamentDataSource.getQualifyingTournamentIds()
        val standings = remoteTournamentDataSource.getTop8Standings(tournamentIds.map { it.id })
        val deckStats = DeckStatsAggregator.aggregate(standings)
        return deckStats.toCalculatedDecks()
    }
}
