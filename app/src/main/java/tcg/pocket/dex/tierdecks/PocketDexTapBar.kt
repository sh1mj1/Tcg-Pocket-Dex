package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PocketDexTopBar(
    openUrl: () -> Unit,
    onSearchClicked: () -> Unit,
    onSettingClicked: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "TCG Pocket Dex",
                modifier =
                    Modifier.clickable(
                        onClick = openUrl,
                    ),
            )
        },
        navigationIcon = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingClicked) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Setting Icon",
                )
            }
        },
    )
}
