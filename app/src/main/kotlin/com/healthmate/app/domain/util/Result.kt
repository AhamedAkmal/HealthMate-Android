package com.healthmate.app.domain.util

/**
 * A generic wrapper for operations that can succeed or fail.
 * Used to safely pass data or errors from the Repository to the ViewModel.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}