package tcg.pocket.dex.datasource

import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail

interface CardsDataSource {
    suspend fun allCards(): List<CardData>

    fun cardDetail(id: String): CardDetail

    fun relatedCards(id: String): List<CardData>
}
