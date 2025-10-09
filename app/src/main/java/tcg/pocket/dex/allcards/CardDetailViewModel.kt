package tcg.pocket.dex.allcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tcg.pocket.dex.repo.allcards.CardsRepo
import tcg.pocket.dex.repo.decks.FakeDecksRepo
import tcg.pocket.dex.tierdecks.DeckInformation
import tcg.pocket.dex.tierdecks.DeckItemState

class CardDetailViewModel(
    cardId: String,
    // TODO: fakeDecksInformation from deck Repo
    relatedDecksInformation: List<DeckInformation> =
        FakeDecksRepo.fakeTierDecksInformation.subList(
            0,
            4,
        ),
    private val cardsRepo: CardsRepo,
) : ViewModel() {
    val cardDetailState: StateFlow<CardDetail?>
        field: MutableStateFlow<CardDetail?> = MutableStateFlow(null)

    val relatedCardsState: StateFlow<List<CardData>>
        field: MutableStateFlow<List<CardData>> = MutableStateFlow(cardsRepo.relatedCards(cardId))

    val relatedDecksState: StateFlow<List<DeckItemState>>
        field: MutableStateFlow<List<DeckItemState>> =
        MutableStateFlow(
            relatedDecksInformation.map(::DeckItemState),
        )

    init {
        viewModelScope.launch {
            cardDetailState.value = cardsRepo.cardDetail(cardId)
        }
    }

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
