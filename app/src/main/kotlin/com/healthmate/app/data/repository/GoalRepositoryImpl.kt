package com.healthmate.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.healthmate.app.domain.model.DailyGoal
import com.healthmate.app.domain.repository.GoalRepository

class GoalRepositoryImpl(context: Context) : GoalRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("healthmate_goals", Context.MODE_PRIVATE)

    override fun saveGoals(goal: DailyGoal) {
        prefs.edit()
            .putInt("steps_goal", goal.stepsGoal)
            .putInt("calories_goal", goal.caloriesGoal)
            .putInt("water_goal", goal.waterGoal)
            .apply()
    }

    override fun getGoals(): DailyGoal? {
        val steps = prefs.getInt("steps_goal", 0)
        val calories = prefs.getInt("calories_goal", 0)
        val water = prefs.getInt("water_goal", 0)

        // If all goals are 0, it means they haven't configured them yet
        return if (steps == 0 && calories == 0 && water == 0) {
            null
        } else {
            DailyGoal(stepsGoal = steps, caloriesGoal = calories, waterGoal = water)
        }
    }
}