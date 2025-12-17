package tcg.pocket.dex.datasource

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import tcg.pocket.dex.remote.response.Standing
import tcg.pocket.dex.remote.response.TournamentId
import tcg.pocket.dex.remote.response.toStanding
import tcg.pocket.dex.remote.response.toTournamentId
import tcg.pocket.dex.remote.service.TournamentStatsService
import timber.log.Timber

class RemoteTournamentDataSource(
    private val tournamentStatsService: TournamentStatsService,
) {
    suspend fun getQualifyingTournamentIds(
        game: String = "POCKET",
        minPlayers: Int = 32,
    ): List<TournamentId> {
        val tournaments = tournamentStatsService.fetchTournaments(game)
        Timber.d("Total tournaments fetched: ${tournaments.size}")

        val withEnoughPlayers = tournaments.filter { it.players > minPlayers }
        Timber.d("Qualifying tournaments (>$minPlayers players): ${withEnoughPlayers.size}")

        return withEnoughPlayers.map { it.toTournamentId() }
    }

    suspend fun getTop8Standings(tournamentIds: List<String>): List<Standing> =
        coroutineScope {
            tournamentIds
                .chunked(5)
                .flatMap { chunk ->
                    val results =
                        chunk.map { id ->
                            async {
                                try {
                                    val standings = tournamentStatsService.fetchStandings(id)
                                    val validStandings =
                                        standings.filter { it.placing != null && it.placing <= 8 }
                                    val invalidCount = standings.size - validStandings.size

                                    if (invalidCount > 0) {
                                        Timber.d("Filtered $invalidCount invalid standings from tournament $id")
                                    }

                                    validStandings
                                } catch (e: Exception) {
                                    Timber.e("Failed to fetch standings for tournament $id: ${e.message}")
                                    emptyList()
                                }
                            }
                        }
                    delay(200)
                    results.awaitAll().flatten()
                }
                .map { it.toStanding() }
        }
}
