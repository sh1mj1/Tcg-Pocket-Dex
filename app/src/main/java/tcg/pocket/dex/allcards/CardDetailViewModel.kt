package tcg.pocket.dex.allcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.fakeCardDetail
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.tierdecks.fakeDecksInformation

class CardDetailViewModel(
    cardId: String,
    cardDetail: CardDetail = fakeCardDetail,
    relatedCards: List<CardData> = fakeCardsData.subList(0, 5),
    relatedDecksInformation: List<DeckInformation> = fakeDecksInformation.subList(0, 4),
) : ViewModel() {
    // TODO: cardId 로 데이터 얻기 from Repository or something
    val cardDetailState: StateFlow<CardDetail>
        field: MutableStateFlow<CardDetail> = MutableStateFlow(cardDetail)

    val relatedCardsState: StateFlow<List<CardData>>
        field: MutableStateFlow<List<CardData>> = MutableStateFlow(relatedCards)

    val relatedDecksState: StateFlow<List<DeckItemState>>
        field: MutableStateFlow<List<DeckItemState>> =
        MutableStateFlow(
            relatedDecksInformation.map(::DeckItemState),
        )

    companion object {
        fun factory(cardId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
                        return CardDetailViewModel(cardId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
