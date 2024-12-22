package com.bizilabs.streeek.feature.tabs.screens.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import coil.compose.AsyncImage
import com.bizilabs.streeek.lib.design.components.SafiCenteredColumn
import com.bizilabs.streeek.lib.design.components.SafiCenteredRow
import com.bizilabs.streeek.lib.design.components.SafiProfileArc

object AchievementsScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel: AchievementsScreenModel = getScreenModel()
        val state by screenModel.state.collectAsStateWithLifecycle()
        AchievementsScreenContent(
            state = state,
            onClickTab = screenModel::onClickTab
        )
    }
}

@Composable
fun AchievementsScreenContent(
    state: AchievementScreenState,
    onClickTab: (AchievementTab) -> Unit,
) {
    Scaffold(
        topBar = { AchievementScreenHeader(state = state, onClickTab = onClickTab) }
    ) { paddingValues ->
        SafiCenteredColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(text = state.tab.label)
        }
    }
}

@Composable
fun AchievementScreenHeader(
    state: AchievementScreenState,
    onClickTab: (AchievementTab) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SafiCenteredColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                state.account?.let { account ->
                    SafiProfileArc(
                        progress = account.points,
                        maxProgress = account.level?.maxPoints ?: account.points.plus(500),
                        modifier = Modifier.size(148.dp),
                        tint = MaterialTheme.colorScheme.primary
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            model = account.avatarUrl,
                            contentDescription = "user avatar url",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = account.username,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = account.level?.name?.replaceFirstChar { it.uppercase() } ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                    Text(
                        text = buildString {
                            append("LV.")
                            append(account.level?.number)
                            append(" | ")
                            append(account.points)
                            append(" EXP")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                }
            }
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = state.tabs.indexOf(state.tab)
            ) {
                state.tabs.forEach { tab ->
                    val isSelected = tab == state.tab
                    val (selectedIcon, unselectedIcon) = tab.icon
                    Tab(
                        selected = isSelected,
                        onClick = { onClickTab(tab) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(0.25f)
                    ) {
                        SafiCenteredRow(modifier = Modifier.padding(16.dp)) {
                            Icon(
                                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                contentDescription = tab.label
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(text = tab.label)
                        }
                    }
                }
            }
        }
    }
}
