package tcg.pocket.dex.remote.service

import tcg.pocket.dex.remote.response.TournamentResponse

interface TournamentStatsService {
    suspend fun fetchTournaments(game: String): List<TournamentResponse>

    companion object {
        const val BASE_URL = "https://play.limitlesstcg.com/api"
    }
}
