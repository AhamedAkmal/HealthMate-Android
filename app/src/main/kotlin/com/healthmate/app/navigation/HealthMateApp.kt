package com.healthmate.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.healthmate.app.data.local.HealthDatabaseHelper
import com.healthmate.app.data.repository.GoalRepositoryImpl
import com.healthmate.app.data.repository.HealthRecordRepositoryImpl
import com.healthmate.app.domain.repository.GoalRepository
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.presentation.addrecord.AddRecordViewModel
import com.healthmate.app.presentation.dashboard.DashboardViewModel
import com.healthmate.app.presentation.editrecord.EditRecordViewModel
import com.healthmate.app.presentation.goals.GoalSettingsViewModel
import com.healthmate.app.presentation.history.HistoryViewModel
import com.healthmate.app.presentation.statistics.StatisticsViewModel
import com.healthmate.app.ui.components.BottomNavigationBar
import com.healthmate.app.ui.components.HealthMateTopBar
import com.healthmate.app.ui.screens.addrecord.AddRecordScreen
import com.healthmate.app.ui.screens.dashboard.DashboardScreen
import com.healthmate.app.ui.screens.editrecord.EditRecordScreen
import com.healthmate.app.ui.screens.goals.GoalSettingsScreen
import com.healthmate.app.ui.screens.history.HistoryScreen
import com.healthmate.app.ui.screens.statistics.StatisticsScreen

@Composable
fun HealthMateApp() {
    var currentRoute by remember { mutableStateOf<NavKey>(Dashboard) }
    val context = LocalContext.current

    val healthRecordRepository: HealthRecordRepository = remember {
        val dbHelper = HealthDatabaseHelper(context.applicationContext)
        HealthRecordRepositoryImpl(dbHelper)
    }

    val goalRepository: GoalRepository = remember {
        GoalRepositoryImpl(context.applicationContext)
    }

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = viewModelFactory {
            initializer { DashboardViewModel(healthRecordRepository, goalRepository) }
        }
    )

    val addRecordViewModel: AddRecordViewModel = viewModel(
        factory = viewModelFactory {
            initializer { AddRecordViewModel(healthRecordRepository) }
        }
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HistoryViewModel(healthRecordRepository) }
        }
    )

    val editRecordViewModel: EditRecordViewModel = viewModel(
        factory = viewModelFactory {
            initializer { EditRecordViewModel(healthRecordRepository) }
        }
    )

    val goalSettingsViewModel: GoalSettingsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { GoalSettingsViewModel(goalRepository) }
        }
    )

    val statisticsViewModel: StatisticsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { StatisticsViewModel(healthRecordRepository) }
        }
    )

    val screenTitle = when (currentRoute) {
        Dashboard -> "HealthMate"
        AddRecord -> "Add Record"
        History -> "History"
        is EditRecord -> "Edit Record"
        is GoalSettings -> "Daily Goals"
        Statistics -> "Weekly Stats" // <-- ADDED THIS
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
                    DashboardScreen(
                        viewModel = dashboardViewModel,
                        onAddRecordClick = { currentRoute = AddRecord },
                        onSetGoalsClick = { currentRoute = GoalSettings }
                    )
                }
                entry<AddRecord> {
                    AddRecordScreen(viewModel = addRecordViewModel)
                }
                entry<History> {
                    HistoryScreen(
                        viewModel = historyViewModel,
                        onEditClick = { id -> currentRoute = EditRecord(id) },
                        onDeleteClick = { id -> currentRoute = EditRecord(id) }
                    )
                }
                entry<EditRecord> { key ->
                    androidx.compose.runtime.LaunchedEffect(key.id) {
                        editRecordViewModel.loadRecord(key.id)
                    }
                    EditRecordScreen(
                        viewModel = editRecordViewModel,
                        onNavigateBack = { currentRoute = History }
                    )
                }
                entry<GoalSettings> {
                    GoalSettingsScreen(
                        viewModel = goalSettingsViewModel,
                        onNavigateBack = { currentRoute = Dashboard }
                    )
                }
                entry<Statistics> { // <-- ADDED THIS
                    StatisticsScreen(viewModel = statisticsViewModel)
                }
            }
        )
    }
}