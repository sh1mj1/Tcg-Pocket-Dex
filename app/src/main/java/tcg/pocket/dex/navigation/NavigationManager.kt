package tcg.pocket.dex.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class NavigationManager {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.TierDecks)

    fun navigateTo(screen: Screen) {
        currentScreen.value = screen
    }
}
