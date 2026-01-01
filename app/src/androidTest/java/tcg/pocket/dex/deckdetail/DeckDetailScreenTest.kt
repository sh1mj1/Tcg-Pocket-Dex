package tcg.pocket.dex.deckdetail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import tcg.pocket.dex.CalculatedDeck
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.common.UiState

class DeckDetailScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loadingState_displaysProgressIndicator() {
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Loading).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithContentDescription("Loading")
            .assertExists()
    }

    @Test
    fun loadingState_noOtherContentVisible() {
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Loading).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Retry")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("No Pokémon information for this deck")
            .assertDoesNotExist()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        val errorMessage = "Network connection failed"
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Error(errorMessage)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysRetryButton() {
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Error("Network error")).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Retry")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_retryButtonClickInvokesRetry() {
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Error("Network error")).asStateFlow()
        every { viewModel.retry() } returns Unit

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Retry")
            .performClick()

        verify(exactly = 1) { viewModel.retry() }
    }

    @Test
    fun errorState_retryButtonClickedMultipleTimes() {
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Error("Failed to load")).asStateFlow()
        every { viewModel.retry() } returns Unit

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Retry")
            .performClick()
        composeTestRule.onNodeWithText("Retry")
            .performClick()
        composeTestRule.onNodeWithText("Retry")
            .performClick()

        verify(exactly = 3) { viewModel.retry() }
    }

    @Test
    fun errorState_customErrorMessage() {
        val errorMessage = "Deck not found: InvalidDeck"
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Error(errorMessage)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_specialCharacters() {
        val errorMessage = "Network error: [500] Internal Server Error!"
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Error(errorMessage)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_displaysEmptyMessage() {
        val emptyData = createTestDeckDetailData(pokemonCards = emptyList())
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(emptyData)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("No Pokémon information for this deck")
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_noDeckContentVisible() {
        val emptyData = createTestDeckDetailData(pokemonCards = emptyList())
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(emptyData)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Win Rate")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("Usage")
            .assertDoesNotExist()
    }

    @Test
    fun successState_displaysDeckName() {
        val deckName = "Pikachu + Mewtwo"
        val data = createTestDeckDetailData(deckName = deckName)
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(deckName)
            .assertIsDisplayed()
    }

    @Test
    fun successState_displaysWinRateStatChip() {
        val winRate = "55.5%"
        val data = createTestDeckDetailData(winRate = winRate)
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Win Rate")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(winRate)
            .assertIsDisplayed()
    }

    @Test
    fun successState_displaysUsageShareStatChip() {
        val usageShare = "25.0%"
        val data = createTestDeckDetailData(usageShare = usageShare)
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Usage")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(usageShare)
            .assertIsDisplayed()
    }

    @Test
    fun successState_displaysPokemonCardNames() {
        val pokemonCards =
            listOf(
                createTestCardDetail("Pikachu"),
                createTestCardDetail("Mewtwo"),
            )
        val data = createTestDeckDetailData(pokemonCards = pokemonCards)
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Pikachu")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Mewtwo")
            .assertIsDisplayed()
    }

    @Test
    fun successState_displaysSectionHeaders() {
        val data = createTestDeckDetailData()
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("━━━ KEY POKÉMON ━━━━━━━━━━━━━━━")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("━━━ ALL POKÉMON ━━━━━━━━━━━━━━━")
            .assertIsDisplayed()
    }

    @Test
    fun successState_singlePokemon() {
        val data =
            createTestDeckDetailData(
                deckName = "Mewtwo Solo",
                pokemonCards = listOf(createTestCardDetail("Mewtwo")),
            )
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Mewtwo")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Win Rate")
            .assertIsDisplayed()
    }

    @Test
    fun successState_longDeckName() {
        val longDeckName = "Pikachu ex + Mewtwo ex + Charizard ex"
        val data =
            createTestDeckDetailData(
                deckName = longDeckName,
                pokemonCards =
                    listOf(
                        createTestCardDetail("Pikachu ex"),
                        createTestCardDetail("Mewtwo ex"),
                        createTestCardDetail("Charizard ex"),
                    ),
            )
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(longDeckName)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Pikachu ex")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Mewtwo ex")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Charizard ex")
            .assertIsDisplayed()
    }

    @Test
    fun successState_cardClickCallbackInvoked() {
        val data =
            createTestDeckDetailData(
                pokemonCards =
                    listOf(
                        createTestCardDetail("Pikachu"),
                        createTestCardDetail("Mewtwo"),
                    ),
            )
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()
        var clickedCardName = ""

        composeTestRule.setContent {
            DeckDetailScreen(
                viewModel = viewModel,
                onCardClick = { clickedCardName = it },
            )
        }

        composeTestRule.onNodeWithText("Pikachu")
            .performClick()

        clickedCardName shouldBe "Pikachu"
    }

    @Test
    fun successState_differentCardClickInvokedCorrectly() {
        val data =
            createTestDeckDetailData(
                pokemonCards =
                    listOf(
                        createTestCardDetail("Pikachu"),
                        createTestCardDetail("Mewtwo"),
                    ),
            )
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()
        var clickedCardName = ""

        composeTestRule.setContent {
            DeckDetailScreen(
                viewModel = viewModel,
                onCardClick = { clickedCardName = it },
            )
        }

        composeTestRule.onNodeWithText("Mewtwo")
            .performClick()

        clickedCardName shouldBe "Mewtwo"
    }

    @Test
    fun successState_extremeWinRate100Percent() {
        val data = createTestDeckDetailData(winRate = "100.0%")
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("100.0%")
            .assertIsDisplayed()
    }

    @Test
    fun successState_extremeWinRate0Percent() {
        val data = createTestDeckDetailData(winRate = "0.0%")
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(data)).asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("0.0%")
            .assertIsDisplayed()
    }

    @Test
    fun stateTransition_loadingToSuccess() {
        val uiStateFlow = MutableStateFlow<UiState<DeckDetailData>>(UiState.Loading)
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithContentDescription("Loading")
            .assertExists()

        val successData =
            createTestDeckDetailData(
                pokemonCards = listOf(createTestCardDetail("Pikachu")),
            )
        uiStateFlow.value = UiState.Success(successData)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Pikachu")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Loading")
            .assertDoesNotExist()
    }

    @Test
    fun stateTransition_successToError() {
        val successData =
            createTestDeckDetailData(
                pokemonCards = listOf(createTestCardDetail("Mewtwo")),
            )
        val uiStateFlow = MutableStateFlow<UiState<DeckDetailData>>(UiState.Success(successData))
        val viewModel = mockk<DeckDetailViewModel>(relaxed = true)
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()

        composeTestRule.setContent {
            DeckDetailScreen(viewModel = viewModel)
        }

        uiStateFlow.value = UiState.Error("Connection lost")

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Connection lost")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Mewtwo")
            .assertDoesNotExist()
    }
}

private fun createTestDeckDetailData(
    deckName: String = "Pikachu + Mewtwo",
    winRate: String = "55.5%",
    usageShare: String = "25.0%",
    pokemonCards: List<CardDetail> =
        listOf(
            createTestCardDetail("Pikachu"),
            createTestCardDetail("Mewtwo"),
        ),
): DeckDetailData {
    return DeckDetailData(
        deck =
            CalculatedDeck(
                deckId = "Pikachu|Mewtwo",
                deckName = deckName,
                winRate = winRate,
                usageShare = usageShare,
                appearances = 100,
            ),
        pokemonCards = pokemonCards,
    )
}

private fun createTestCardDetail(name: String): CardDetail {
    return CardDetail(
        name = name,
        hp = "60",
        type = "Electric",
        typeIcon = 0,
        rarity = "Rare",
        rarityIcon = 0,
        weakness = "20",
        weaknessType = "Fighting",
        weaknessTypeIcon = 0,
        retreatCost = 1,
        stage = 0,
        description = "Test Pokemon",
        imageUrl = "https://example.com/${name.lowercase()}.png",
        pokemonMoves = emptyList(),
    )
}
