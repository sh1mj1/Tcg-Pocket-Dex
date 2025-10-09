package tcg.pocket.dex.repo.allcards

import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail

interface CardsRepo {
    suspend fun allCards(): List<CardData>

    suspend fun cardDetail(id: String): CardDetail

    fun relatedCards(id: String): List<CardData>
}
