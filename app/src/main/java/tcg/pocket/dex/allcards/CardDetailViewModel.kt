package tcg.pocket.dex.allcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tcg.pocket.dex.repo.allcards.CardsRepo
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.fakeDecksInformation

class CardDetailViewModel(
    cardId: String,
    // TODO: fakeDecksInformation from deck Repo
    relatedDecksInformation: List<DeckInformation> = fakeDecksInformation.subList(0, 4),
    cardsRepo: CardsRepo,
) : ViewModel() {
    val cardDetailState: StateFlow<CardDetail>
        field: MutableStateFlow<CardDetail> = MutableStateFlow(cardsRepo.cardDetail(cardId))

    val relatedCardsState: StateFlow<List<CardData>>
        field: MutableStateFlow<List<CardData>> = MutableStateFlow(cardsRepo.relatedCards(cardId))

    val relatedDecksState: StateFlow<List<DeckItemState>>
        field: MutableStateFlow<List<DeckItemState>> =
        MutableStateFlow(
            relatedDecksInformation.map(::DeckItemState),
        )

    companion object {
        fun factory(
            cardId: String,
            cardsRepo: CardsRepo,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
                        return CardDetailViewModel(cardId = cardId, cardsRepo = cardsRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
