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

class RemoteTournamentDataSource(
    private val tournamentStatsService: TournamentStatsService,
) {
    suspend fun getQualifyingTournamentIds(
        game: String = "POCKET",
        minPlayers: Int = 32,
    ): List<TournamentId> {
        return tournamentStatsService
            .fetchTournaments(game)
            .filter { it.players > minPlayers }
            .map { it.toTournamentId() }
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
                                    tournamentStatsService.fetchStandings(id).standings
                                        .filter { it.placing <= 8 }
                                } catch (e: Exception) {
                                    println("Failed to fetch standings for tournament $id: ${e.message}")
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
