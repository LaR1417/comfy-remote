package com.comfyui.remote.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Workflow
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Queue
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Workflow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Connection : Screen(
        route = "connection",
        title = "连接",
        selectedIcon = Icons.Filled.Workflow,
        unselectedIcon = Icons.Outlined.Workflow
    )

    data object Workflow : Screen(
        route = "workflow",
        title = "工作流",
        selectedIcon = Icons.Filled.Workflow,
        unselectedIcon = Icons.Outlined.Workflow
    )

    data object Queue : Screen(
        route = "queue",
        title = "队列",
        selectedIcon = Icons.Filled.Queue,
        unselectedIcon = Icons.Outlined.Queue
    )

    data object History : Screen(
        route = "history",
        title = "历史",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    )

    data object Settings : Screen(
        route = "settings",
        title = "设置",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

val bottomNavItems = listOf(
    Screen.Connection,
    Screen.Workflow,
    Screen.Queue,
    Screen.History,
    Screen.Settings
)
