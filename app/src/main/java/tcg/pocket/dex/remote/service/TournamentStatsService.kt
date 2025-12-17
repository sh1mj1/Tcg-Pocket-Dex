package tcg.pocket.dex.remote.service

import tcg.pocket.dex.remote.response.StandingResponse
import tcg.pocket.dex.remote.response.TournamentResponse

interface TournamentStatsService {
    suspend fun fetchTournaments(game: String): List<TournamentResponse>

    suspend fun fetchStandings(tournamentId: String): List<StandingResponse>

    companion object {
        const val BASE_URL = "https://play.limitlesstcg.com/api"
    }
}
