package com.healthmate.app.domain.repository

import com.healthmate.app.domain.model.DailyGoal

interface GoalRepository {
    fun saveGoals(goal: DailyGoal)
    fun getGoals(): DailyGoal?
}