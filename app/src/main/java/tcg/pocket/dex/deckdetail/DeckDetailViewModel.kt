package tcg.pocket.dex.deckdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.pocket.dex.common.UiState
import tcg.pocket.dex.repo.allcards.CardsRepo
import tcg.pocket.dex.repo.tournamentstats.TournamentStatsRepo
import timber.log.Timber

class DeckDetailViewModel(
    val deckId: String,
    private val tournamentStatsRepo: TournamentStatsRepo,
    private val cardsRepo: CardsRepo,
) : ViewModel() {
    val uiState: StateFlow<UiState<DeckDetailData>>
        field = MutableStateFlow<UiState<DeckDetailData>>(UiState.Loading)

    private var loadJob: Job? = null

    init {
        loadDeckDetail()
    }

    private fun loadDeckDetail() {
        loadJob?.cancel()
        loadJob =
            viewModelScope.launch {
                uiState.update { UiState.Loading }
                try {
                    val allDecks = tournamentStatsRepo.getDeckStatistics()
                    val deck =
                        allDecks.find { it.deckId == deckId }
                            ?: throw IllegalArgumentException("Deck not found: $deckId")

                    val pokemonNames =
                        deck.deckId
                            .split("|")
                            .filter { it.isNotBlank() }
                    Timber.d("Loading cards for Pokemon: $pokemonNames")

                    val pokemonCards =
                        pokemonNames
                            .map { name ->
                                async { searchAndFetchPokemon(name) }
                            }.awaitAll()
                            .filterNotNull()

                    if (pokemonCards.isEmpty()) {
                        Timber.w("No Pokemon cards found for deck: $deckId")
                    }

                    uiState.update { UiState.Success(DeckDetailData(deck, pokemonCards)) }
                } catch (e: Exception) {
                    Timber.e(e, "Error loading deck details")
                    uiState.update { UiState.Error(e.message ?: "Failed to load deck details") }
                }
            }
    }

    private suspend fun searchAndFetchPokemon(name: String): tcg.pocket.dex.allcards.CardDetail? {
        val trimmedName = name.trim()
        return try {
            cardsRepo.cardDetail(trimmedName)
        } catch (e: Exception) {
            try {
                cardsRepo.cardDetail("$trimmedName ex")
            } catch (e: Exception) {
                Timber.w("Failed to fetch Pokemon: $trimmedName")
                null
            }
        }
    }

    fun retry() {
        loadDeckDetail()
    }

    companion object {
        fun factory(
            deckId: String,
            tournamentStatsRepo: TournamentStatsRepo,
            cardsRepo: CardsRepo,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(DeckDetailViewModel::class.java)) {
                        return DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = tournamentStatsRepo,
                            cardsRepo = cardsRepo,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
