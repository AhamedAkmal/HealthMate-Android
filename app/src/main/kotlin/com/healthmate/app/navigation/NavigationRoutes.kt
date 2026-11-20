package com.healthmate.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Dashboard : NavKey

@Serializable
data object AddRecord : NavKey

@Serializable
data object History : NavKey

@Serializable
data class EditRecord(val id: Long) : NavKey

@Serializable
data object GoalSettings : NavKey

@Serializable
data object Statistics : NavKey