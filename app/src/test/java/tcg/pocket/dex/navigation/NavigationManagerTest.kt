package tcg.pocket.dex.navigation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NavigationManagerTest : StringSpec({
    "navigateTo adds screen to backstack" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.TierDecks),
            )
        // when
        navigationManager.navigateTo(Screen.Search)
        navigationManager.navigateTo(Screen.Setting)

        // then
        navigationManager.backstack shouldBe
            listOf(
                Screen.TierDecks,
                Screen.Search,
                Screen.Setting,
            )
    }

    "navigateBack removes screen from backstack" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.TierDecks, Screen.Search),
            )

        // when
        navigationManager.navigateBack()

        // then
        navigationManager.currentScreen shouldBe Screen.TierDecks
    }

    "NavigationManger with empty backstack at initialization throws exception" {
        // then
        shouldThrow<IllegalArgumentException> {
            // when
            NavigationManager(initialBackstack = emptyList())
        }
    }

    "navigateBack when backstack is empty throws exception" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.TierDecks),
            )

        // then
        shouldThrow<IllegalStateException> {
            navigationManager.navigateBack()
        }
    }

    "navigate to DeckDetailScreen with deckId" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.TierDecks),
            )

        // when
        navigationManager.navigateTo(Screen.DeckDetail("deck1"))

        // then
        navigationManager.currentScreen shouldBe Screen.DeckDetail("deck1")
    }
})
