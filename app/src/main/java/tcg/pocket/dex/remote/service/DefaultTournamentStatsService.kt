package tcg.pocket.dex.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tcg.pocket.dex.BuildConfig
import tcg.pocket.dex.remote.response.StandingResponse
import tcg.pocket.dex.remote.response.TournamentResponse
import tcg.pocket.dex.remote.service.TournamentStatsService.Companion.BASE_URL

class DefaultTournamentStatsService(
    private val client: HttpClient = httpClient,
    private val baseUrl: String = BASE_URL,
    private val apiKey: String = BuildConfig.LIMITLESS_API_KEY,
) : TournamentStatsService {
    override suspend fun fetchTournaments(game: String): List<TournamentResponse> =
        client.get("$baseUrl/tournaments?game=$game&key=$apiKey").body()

    override suspend fun fetchStandings(tournamentId: String): List<StandingResponse> =
        client.get("$baseUrl/tournaments/$tournamentId/standings?key=$apiKey").body()
}
