package tcg.pocket.dex.datasource

import tcg.pocket.dex.allcards.CardData
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.remote.response.BriefCardResponse
import tcg.pocket.dex.remote.service.CardsService

class RemoteCardsDataSource(
    private val cardsService: CardsService,
) : CardsDataSource {
    override suspend fun allCards(): List<CardData> {
        return cardsService.briefCards().toCardDataList()
    }

    override fun cardDetail(id: String): CardDetail {
        TODO("Not yet implemented")
    }

    override fun relatedCards(id: String): List<CardData> {
        TODO("Not yet implemented")
    }
}

// TODO: image 가 null 이어도 처리
//  어디서 문제인지 모르지만 카드 뷰 계속 보고 있으면 스크린 나가짐.
//  다시 들어가면 터짐.
//  LiveEdit: Error instantiating superclass: Ltcg/pocket/dex/datasource/RemoteCardsDataSource$allCards$1;.
private fun BriefCardResponse.toCardData(): CardData? {
    if (this.image == null) {
        return null
    }
    return CardData(
        id = this.id,
        name = this.name,
        imageUrl = this.image + "/low.webp",
        rarityUrl = "",
        typeUrl = "",
    )
}

private fun List<BriefCardResponse>.toCardDataList(): List<CardData> = this.mapNotNull(BriefCardResponse::toCardData)
