package tcg.pocket.dex.repo.tournamentstats

import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.DeckStatsAggregator
import tcg.pocket.dex.datasource.RemoteTournamentDataSource
import tcg.pocket.dex.toCalculatedDecks
import timber.log.Timber

class DefaultTournamentStatsRepo(
    private val remoteTournamentDataSource: RemoteTournamentDataSource,
) : TournamentStatsRepo {
    override suspend fun getDeckStatistics(): List<CalculatedDeck> {
        val tournamentIds = remoteTournamentDataSource.getQualifyingTournamentIds()
        Timber.d("Found ${tournamentIds.size} qualifying tournaments")

        val standings = remoteTournamentDataSource.getTop8Standings(tournamentIds.map { it.id })
        Timber.d("Total standings fetched: ${standings.size}")
        Timber.d("Standings WITH deck data: ${standings.count { it.deckPokemon.isNotEmpty() }}")
        Timber.d("Standings WITHOUT deck data: ${standings.count { it.deckPokemon.isEmpty() }}")

        standings.take(5).forEachIndexed { index, standing ->
            Timber.d("Standing[$index]: player=${standing.playerName}, deck=${standing.deckPokemon}")
        }

        val deckStats = DeckStatsAggregator.aggregate(standings)
        Timber.d("Aggregated into ${deckStats.size} unique decks")

        return deckStats.toCalculatedDecks()
    }
}
