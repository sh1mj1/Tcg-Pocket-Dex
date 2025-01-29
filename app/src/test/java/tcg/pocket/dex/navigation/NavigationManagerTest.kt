package tcg.pocket.dex.navigation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NavigationManagerTest : StringSpec({
    "navigateTo adds screen to backstack" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.BottomNavigation.TierDecks),
            )
        // when
        navigationManager.navigateTo(Screen.Search)
        navigationManager.navigateTo(Screen.Setting)

        // then
        navigationManager.backstack shouldBe
            listOf(
                Screen.BottomNavigation.TierDecks,
                Screen.Search,
                Screen.Setting,
            )
    }

    "navigateBack removes screen from backstack" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.BottomNavigation.TierDecks, Screen.Search),
            )

        // when
        navigationManager.navigateBack()

        // then
        navigationManager.currentScreen shouldBe Screen.BottomNavigation.TierDecks
    }

    "NavigationManger with empty backstack at initialization throws exception" {
        // then
        shouldThrow<IllegalArgumentException> {
            // when
            NavigationManager(
                initialBackstack = emptyList(),
            )
        }
    }

    "navigateBack when backstack has only one stack then do not remove stack" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.BottomNavigation.TierDecks),
            )

        // then
        navigationManager.backstack shouldBe listOf(Screen.BottomNavigation.TierDecks)
    }

    "navigate to DeckDetailScreen with deckId" {
        // given
        val navigationManager =
            NavigationManager(
                initialBackstack = listOf(Screen.BottomNavigation.TierDecks),
            )

        // when
        navigationManager.navigateTo(Screen.DeckDetail("deck1"))

        // then
        navigationManager.currentScreen shouldBe Screen.DeckDetail("deck1")
    }
})
