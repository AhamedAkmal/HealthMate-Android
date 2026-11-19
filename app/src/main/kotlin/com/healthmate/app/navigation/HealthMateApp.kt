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
import com.healthmate.app.data.repository.HealthRecordRepositoryImpl
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.presentation.addrecord.AddRecordViewModel
import com.healthmate.app.presentation.editrecord.EditRecordViewModel
import com.healthmate.app.presentation.history.HistoryViewModel
import com.healthmate.app.ui.components.BottomNavigationBar
import com.healthmate.app.ui.components.HealthMateTopBar
import com.healthmate.app.ui.screens.addrecord.AddRecordScreen
import com.healthmate.app.ui.screens.dashboard.DashboardScreen
import com.healthmate.app.ui.screens.editrecord.EditRecordScreen
import com.healthmate.app.ui.screens.history.HistoryScreen

@Composable
fun HealthMateApp() {
    var currentRoute by remember { mutableStateOf<NavKey>(Dashboard) }
    val context = LocalContext.current

    val repository: HealthRecordRepository = remember {
        val dbHelper = HealthDatabaseHelper(context.applicationContext)
        HealthRecordRepositoryImpl(dbHelper)
    }

    val addRecordViewModel: AddRecordViewModel = viewModel(
        factory = viewModelFactory {
            initializer { AddRecordViewModel(repository) }
        }
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HistoryViewModel(repository) }
        }
    )

    val editRecordViewModel: EditRecordViewModel = viewModel(
        factory = viewModelFactory {
            initializer { EditRecordViewModel(repository) }
        }
    )

    val screenTitle = when (currentRoute) {
        Dashboard -> "HealthMate"
        AddRecord -> "Add Record"
        History -> "History"
        is EditRecord -> "Edit Record"
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
                        onAddRecordClick = { currentRoute = AddRecord }
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
                    // Load the record when this screen is created
                    androidx.compose.runtime.LaunchedEffect(key.id) {
                        editRecordViewModel.loadRecord(key.id)
                    }
                    EditRecordScreen(
                        viewModel = editRecordViewModel,
                        onNavigateBack = { currentRoute = History }
                    )
                }
            }
        )
    }
}