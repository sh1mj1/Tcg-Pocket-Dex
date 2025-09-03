package tcg.pocket.dex.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tcg.pocket.dex.remote.response.BriefCardResponse
import tcg.pocket.dex.remote.service.CardsService.Companion.BASE_ENGLISH_URL

// TODO:Dependency Injection for HttpClient
class DefaultCardsService(
    private val client: HttpClient = httpClient,
    private val baseUrl: String = BASE_ENGLISH_URL,
) : CardsService {
    override suspend fun briefCards(): List<BriefCardResponse> = client.get("$baseUrl/cards").body()
}
