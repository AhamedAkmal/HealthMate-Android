package com.healthmate.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.healthmate.app.ui.components.BottomNavigationBar
import com.healthmate.app.ui.components.HealthMateTopBar
import com.healthmate.app.ui.screens.addrecord.AddRecordScreen
import com.healthmate.app.ui.screens.dashboard.DashboardScreen
import com.healthmate.app.ui.screens.history.HistoryScreen

@Composable
fun HealthMateApp() {
    var currentRoute by remember { mutableStateOf<NavKey>(Dashboard) }

    val screenTitle = when (currentRoute) {
        Dashboard -> "HealthMate"
        AddRecord -> "Add Record"
        History -> "History"
        else -> "HealthMate"
    }

    Scaffold(
        topBar = {
            HealthMateTopBar(title = screenTitle)
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = currentRoute,
                onItemClick = { route -> currentRoute = route }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = listOf(currentRoute),
            modifier = Modifier.padding(innerPadding),
            onBack = {
                if (currentRoute != Dashboard) {
                    currentRoute = Dashboard
                }
            },
            entryProvider = entryProvider {
                entry<Dashboard> {
                    DashboardScreen()
                }
                entry<AddRecord> {
                    AddRecordScreen()
                }
                entry<History> {
                    HistoryScreen()
                }
            }
        )
    }
}