package com.comfyui.remote.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.comfyui.remote.ui.screens.connection.ConnectionScreen
import com.comfyui.remote.ui.screens.history.HistoryScreen
import com.comfyui.remote.ui.screens.queue.QueueScreen
import com.comfyui.remote.ui.screens.settings.SettingsScreen
import com.comfyui.remote.ui.screens.workflow.WorkflowScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComfyUINavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = bottomNavItems.find { screen ->
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        }?.title ?: "ComfyUI Remote"
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Connection.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Connection.route) {
                ConnectionScreen()
            }
            composable(Screen.Workflow.route) {
                WorkflowScreen()
            }
            composable(Screen.Queue.route) {
                QueueScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
