package tcg.pocket.dex.remote.service

import tcg.pocket.dex.remote.response.BriefCardsResponse
import tcg.pocket.dex.remote.response.CardDetailResponse

interface CardsService {
    suspend fun briefCards(setId: String): BriefCardsResponse

    suspend fun cardDetail(id: String): CardDetailResponse

    companion object {
        const val BASE_ENGLISH_URL = "https://api.tcgdex.net/v2/en"
    }
}
