package tcg.pocket.dex.datasource

import tcg.pocket.dex.remote.response.TournamentId
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
}
