package tcg.pocket.dex.deckdetail

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.common.UiState
import tcg.pocket.dex.repo.allcards.CardsRepo
import tcg.pocket.dex.repo.tournamentstats.TournamentStatsRepo

@OptIn(ExperimentalCoroutinesApi::class)
class DeckDetailViewModelTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    Given("DeckDetailViewModel with valid repositories") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu|Mewtwo"

        When("덱과 모든 포켓몬 카드를 성공적으로 로드하면") {
            Then("Success 상태가 되어야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    val pikachuCard = createCardDetail(name = "Pikachu")
                    val mewtwoCard = createCardDetail(name = "Mewtwo")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns pikachuCard
                    coEvery { mockCardsRepo.cardDetail("Mewtwo") } returns mewtwoCard

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()
                    val data = (state as UiState.Success).data
                    data.deck shouldBe deck
                    data.pokemonCards shouldHaveSize 2
                    data.pokemonCards[0].name shouldBe "Pikachu"
                    data.pokemonCards[1].name shouldBe "Mewtwo"
                }
            }
        }

        When("덱이 존재하지 않으면") {
            Then("Error 상태가 되어야 한다") {
                runTest(testDispatcher) {
                    coEvery { mockTournamentStatsRepo.getDeckById("NonExistent") } returns null

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = "NonExistent",
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Error>()
                    (state as UiState.Error).message shouldBe "Deck not found: NonExistent"
                }
            }
        }

        When("덱 통계에서 일치하는 덱이 없으면") {
            Then("Error 상태가 되어야 한다") {
                runTest(testDispatcher) {
                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns null

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Error>()
                    (state as UiState.Error).message shouldBe "Deck not found: $deckId"
                }
            }
        }
    }

    Given("DeckDetailViewModel with partial card fetch failures") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu|Mewtwo|Charizard"

        When("일부 포켓몬 카드만 성공적으로 로드되면") {
            Then("Success 상태이고 성공한 카드만 포함되어야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    val pikachuCard = createCardDetail(name = "Pikachu")
                    val mewtwoCard = createCardDetail(name = "Mewtwo")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns pikachuCard
                    coEvery { mockCardsRepo.cardDetail("Mewtwo") } returns mewtwoCard
                    coEvery { mockCardsRepo.cardDetail("Charizard") } throws RuntimeException("Card not found")
                    coEvery { mockCardsRepo.cardDetail("Charizard ex") } throws RuntimeException("Card not found")

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()
                    val data = (state as UiState.Success).data
                    data.deck shouldBe deck
                    data.pokemonCards shouldHaveSize 2
                    data.pokemonCards[0].name shouldBe "Pikachu"
                    data.pokemonCards[1].name shouldBe "Mewtwo"

                    coVerify { mockCardsRepo.cardDetail("Charizard") }
                    coVerify { mockCardsRepo.cardDetail("Charizard ex") }
                }
            }
        }
    }

    Given("DeckDetailViewModel with all card fetch failures") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu|Mewtwo"

        When("모든 포켓몬 카드 로드가 실패하면") {
            Then("Success 상태이고 빈 카드 리스트가 되어야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } throws RuntimeException("Card not found")
                    coEvery { mockCardsRepo.cardDetail("Pikachu ex") } throws RuntimeException("Card not found")
                    coEvery { mockCardsRepo.cardDetail("Mewtwo") } throws RuntimeException("Card not found")
                    coEvery { mockCardsRepo.cardDetail("Mewtwo ex") } throws RuntimeException("Card not found")

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()
                    val data = (state as UiState.Success).data
                    data.deck shouldBe deck
                    data.pokemonCards.shouldBeEmpty()
                }
            }
        }
    }

    Given("DeckDetailViewModel with name fallback logic") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Mewtwo"

        When("정확한 이름으로 실패하고 'ex' 추가로 성공하면") {
            Then("Success 상태가 되고 'ex' 카드를 반환해야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    val mewtwoExCard = createCardDetail(name = "Mewtwo ex")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Mewtwo") } throws RuntimeException("Card not found")
                    coEvery { mockCardsRepo.cardDetail("Mewtwo ex") } returns mewtwoExCard

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()
                    val data = (state as UiState.Success).data
                    data.pokemonCards shouldHaveSize 1
                    data.pokemonCards[0].name shouldBe "Mewtwo ex"

                    coVerify { mockCardsRepo.cardDetail("Mewtwo") }
                    coVerify { mockCardsRepo.cardDetail("Mewtwo ex") }
                }
            }
        }

        When("정확한 이름으로 성공하면") {
            Then("Success 상태가 되고 정확한 이름의 카드를 반환해야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    val mewtwoCard = createCardDetail(name = "Mewtwo")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Mewtwo") } returns mewtwoCard

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()
                    val data = (state as UiState.Success).data
                    data.pokemonCards shouldHaveSize 1
                    data.pokemonCards[0].name shouldBe "Mewtwo"
                }
            }
        }

        When("이름에 공백이 포함되어 있으면") {
            Then("trim된 이름으로 카드를 조회해야 한다") {
                runTest(testDispatcher) {
                    val deckIdWithSpaces = " Pikachu "
                    val deck = createCalculatedDeck(deckId = deckIdWithSpaces)
                    val pikachuCard = createCardDetail(name = "Pikachu")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckIdWithSpaces) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns pikachuCard

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckIdWithSpaces,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()

                    coVerify { mockCardsRepo.cardDetail("Pikachu") }
                }
            }
        }
    }

    Given("DeckDetailViewModel with network errors") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu"

        When("TournamentStatsRepo에서 예외가 발생하면") {
            Then("Error 상태가 되어야 한다") {
                runTest(testDispatcher) {
                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } throws RuntimeException("Network error")

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Error>()
                    (state as UiState.Error).message shouldBe "Network error"
                }
            }
        }

        When("예외 메시지가 null이면") {
            Then("기본 에러 메시지를 사용해야 한다") {
                runTest(testDispatcher) {
                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } throws RuntimeException()

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Error>()
                    (state as UiState.Error).message shouldBe "Failed to load deck details"
                }
            }
        }
    }

    Given("DeckDetailViewModel with retry functionality") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu"

        When("Error 상태에서 retry를 호출하면") {
            Then("데이터를 다시 로드해야 한다") {
                runTest(testDispatcher) {
                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } throws RuntimeException("Network error") andThen
                        createCalculatedDeck(deckId = deckId)
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns createCardDetail(name = "Pikachu")

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val errorState = viewModel.uiState.value
                    errorState.shouldBeInstanceOf<UiState.Error>()

                    viewModel.retry()
                    advanceUntilIdle()

                    val successState = viewModel.uiState.value
                    successState.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()

                    coVerify(exactly = 2) { mockTournamentStatsRepo.getDeckById(deckId) }
                }
            }
        }

        When("Success 상태에서 retry를 호출하면") {
            Then("데이터를 다시 로드해야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    val pikachuCard = createCardDetail(name = "Pikachu")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns pikachuCard
                    coEvery { mockCardsRepo.cardDetail("Pikachu ex") } returns createCardDetail(name = "Pikachu ex")

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val initialState = viewModel.uiState.value
                    initialState.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()

                    viewModel.retry()
                    advanceUntilIdle()

                    val retryState = viewModel.uiState.value
                    retryState.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()

                    coVerify(atLeast = 2) { mockTournamentStatsRepo.getDeckById(deckId) }
                    coVerify(atLeast = 2) { mockCardsRepo.cardDetail("Pikachu") }
                }
            }
        }
    }

    Given("DeckDetailViewModel with multiple Pokemon") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu|Mewtwo|Charizard|Blastoise"

        When("4개의 포켓몬이 모두 성공적으로 로드되면") {
            Then("Success 상태가 되고 4개의 카드가 순서대로 반환되어야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    val pikachuCard = createCardDetail(name = "Pikachu")
                    val mewtwoCard = createCardDetail(name = "Mewtwo")
                    val charizardCard = createCardDetail(name = "Charizard")
                    val blastoiseCard = createCardDetail(name = "Blastoise")

                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns pikachuCard
                    coEvery { mockCardsRepo.cardDetail("Mewtwo") } returns mewtwoCard
                    coEvery { mockCardsRepo.cardDetail("Charizard") } returns charizardCard
                    coEvery { mockCardsRepo.cardDetail("Blastoise") } returns blastoiseCard

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Success<DeckDetailData>>()
                    val data = (state as UiState.Success).data
                    data.pokemonCards shouldHaveSize 4
                    data.pokemonCards[0].name shouldBe "Pikachu"
                    data.pokemonCards[1].name shouldBe "Mewtwo"
                    data.pokemonCards[2].name shouldBe "Charizard"
                    data.pokemonCards[3].name shouldBe "Blastoise"
                }
            }
        }
    }

    Given("DeckDetailViewModel with Loading state") {
        val mockTournamentStatsRepo = mockk<TournamentStatsRepo>()
        val mockCardsRepo = mockk<CardsRepo>()
        val deckId = "Pikachu"

        When("ViewModel이 생성되면") {
            Then("초기 상태는 Loading이어야 한다") {
                runTest(testDispatcher) {
                    val deck = createCalculatedDeck(deckId = deckId)
                    coEvery { mockTournamentStatsRepo.getDeckById(deckId) } returns deck
                    coEvery { mockCardsRepo.cardDetail("Pikachu") } returns createCardDetail(name = "Pikachu")

                    val viewModel =
                        DeckDetailViewModel(
                            deckId = deckId,
                            tournamentStatsRepo = mockTournamentStatsRepo,
                            cardsRepo = mockCardsRepo,
                        )

                    viewModel.uiState.value.shouldBeInstanceOf<UiState.Loading>()
                }
            }
        }
    }
})

private fun createCalculatedDeck(
    deckId: String = "Pikachu",
    deckName: String = "Pikachu",
    winRate: String = "50.0",
    usageShare: String = "25.0",
    appearances: Int = 10,
    iconUrls: List<String> = emptyList(),
) = CalculatedDeck(
    deckId = deckId,
    deckName = deckName,
    winRate = winRate,
    usageShare = usageShare,
    appearances = appearances,
    iconUrls = iconUrls,
)

private fun createCardDetail(
    name: String = "Pikachu",
    category: String = "Pokemon",
    rarity: String = "Common",
    type: String = "Electric",
    hp: String = "60",
    retreatCost: Int = 1,
    stage: Int = 0,
) = CardDetail(
    category = category,
    name = name,
    rarity = rarity,
    rarityIcon = 0,
    type = type,
    typeIcon = 0,
    weakness = "20",
    weaknessType = "Fighting",
    weaknessTypeIcon = 0,
    hp = hp,
    retreatCost = retreatCost,
    stage = stage,
    description = "Test Pokemon",
    imageUrl = "https://example.com/${name.lowercase()}.png",
    pokemonMoves = emptyList(),
)
