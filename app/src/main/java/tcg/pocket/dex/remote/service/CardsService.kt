package tcg.pocket.dex.remote.service

import tcg.pocket.dex.remote.response.BriefCardResponse

interface CardsService {
    suspend fun briefCards(): List<BriefCardResponse>

    companion object {
        const val BASE_ENGLISH_URL = "https://api.tcgdex.net/v2/en"
    }
}
