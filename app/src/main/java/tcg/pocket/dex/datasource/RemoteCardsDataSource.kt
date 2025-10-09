package tcg.pocket.dex.datasource

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.remote.response.BriefCard
import tcg.pocket.dex.remote.response.toCardDetail
import tcg.pocket.dex.remote.service.CardsService

class RemoteCardsDataSource(
    private val cardsService: CardsService,
) : CardsDataSource {
    // TODO: This should be called with the setIds from the result of https://api.tcgdex.net/v2/en/series/tcgp
    //  Therefore, the setIds property below should later be used to fetch cards based on the results from a server call.
    //  Caching could also be considered in the future.
    private val setIds =
        listOf("P-A", "A1", "A1a", "A2", "A2a", "A2b", "A3", "A3a", "A3b", "A4", "A4a")

    override suspend fun allCards(): List<CardData> =
        coroutineScope {
            val deferredCards =
                setIds.map { setId ->
                    async {
                        cardsService.briefCards(setId).cards
                    }
                }
            deferredCards.awaitAll().flatten().toCardDataList()
        }

    override suspend fun cardDetail(id: String): CardDetail {
        return cardsService.cardDetail(id).toCardDetail()
    }

    override fun relatedCards(id: String): List<CardData> {
        TODO("Not yet implemented")
    }
}

private fun BriefCard.toCardData(): CardData? {
    if (this.image == null) {
        return null
    }
    return CardData(
        id = this.id,
        name = this.name,
        imageUrl = this.image + "/low.webp",
        // TODO: This will be filled later if needed
        rarityUrl = "",
        // TODO: This will be filled later if needed'
        typeUrl = "",
    )
}

private fun List<BriefCard>.toCardDataList(): List<CardData> = this.mapNotNull(BriefCard::toCardData)
