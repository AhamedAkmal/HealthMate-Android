package com.healthmate.app.presentation.addrecord

import java.time.LocalDate

object AddRecordValidator {

    fun validate(state: AddRecordUiState): AddRecordUiState {
        var newState = state.copy(saveError = null)

        // Date validation
        if (state.date.isAfter(LocalDate.now())) {
            newState = newState.copy(dateError = "Date cannot be in the future")
        } else {
            newState = newState.copy(dateError = null)
        }

        // Steps validation
        val stepsInt = state.steps.toIntOrNull()
        if (state.steps.isBlank()) {
            newState = newState.copy(stepsError = "Steps cannot be empty")
        } else if (stepsInt == null || stepsInt < 0) {
            newState = newState.copy(stepsError = "Enter a valid positive number")
        } else {
            newState = newState.copy(stepsError = null)
        }

        // Calories validation
        val caloriesInt = state.calories.toIntOrNull()
        if (state.calories.isBlank()) {
            newState = newState.copy(caloriesError = "Calories cannot be empty")
        } else if (caloriesInt == null || caloriesInt < 0) {
            newState = newState.copy(caloriesError = "Enter a valid positive number")
        } else {
            newState = newState.copy(caloriesError = null)
        }

        // Water validation
        val waterInt = state.water.toIntOrNull()
        if (state.water.isBlank()) {
            newState = newState.copy(waterError = "Water intake cannot be empty")
        } else if (waterInt == null || waterInt < 0) {
            newState = newState.copy(waterError = "Enter a valid positive number")
        } else {
            newState = newState.copy(waterError = null)
        }

        return newState
    }

    fun isValid(state: AddRecordUiState): Boolean {
        return state.dateError == null &&
               state.stepsError == null &&
               state.caloriesError == null &&
               state.waterError == null &&
               state.steps.isNotBlank() &&
               state.calories.isNotBlank() &&
               state.water.isNotBlank()
    }
}