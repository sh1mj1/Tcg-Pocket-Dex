package tcg.pocket.dex.repo.allcards

import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.datasource.RemoteCardsDataSource
import tcg.pocket.dex.repo.allcards.FakeCardsRepo.Companion.fakeRelatedCards

class DefaultCardsRepo(
    private val remoteCardsDataSource: RemoteCardsDataSource,
) : CardsRepo {
    override suspend fun allCards(): List<CardData> = remoteCardsDataSource.allCards()

    override suspend fun cardDetail(id: String): CardDetail = remoteCardsDataSource.cardDetail(id)

    override fun relatedCards(id: String): List<CardData> = fakeRelatedCards // TODO: modify
}
