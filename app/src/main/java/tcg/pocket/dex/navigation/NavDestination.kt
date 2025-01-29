package tcg.pocket.dex.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

// TODO: create object lazily?
interface NavDestination {
    val route: String
}

interface BottomNavDestination : NavDestination {
    val icon: ImageVector
}

object AllCards : BottomNavDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val route = "all_cards"
}

object TierDecks : BottomNavDestination {
    override val icon = Icons.Default.Star
    override val route = "accounts"
}

object ExpansionPacks : BottomNavDestination {
    override val icon = Icons.Default.AccountBox
    override val route = "bills"
}

val bottomRowScreens = listOf(AllCards, TierDecks, ExpansionPacks)
