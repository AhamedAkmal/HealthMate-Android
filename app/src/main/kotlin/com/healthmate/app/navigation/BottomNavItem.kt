package com.healthmate.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: NavKey
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Dashboard",
        icon = Icons.Filled.Home,
        route = Dashboard
    ),
    BottomNavItem(
        label = "Add",
        icon = Icons.Filled.Add,
        route = AddRecord
    ),
    BottomNavItem(
        label = "History",
        icon = Icons.Filled.List,
        route = History
    ),
    BottomNavItem(
        label = "Stats",
        icon = Icons.Filled.Star,
        route = Statistics
    )
)