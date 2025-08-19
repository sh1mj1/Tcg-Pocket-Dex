package tcg.pocket.dex.repo.allcards

import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail

interface CardsRepo {
    suspend fun allCards(): List<CardData>

    fun cardDetail(id: String): CardDetail

    fun relatedCards(id: String): List<CardData>
}
