package com.pay.workoutapp.utils

import android.content.Context
import android.content.Intent
import com.pay.workoutapp.ui.BMIActivity
import com.pay.workoutapp.ui.ExerciseActivity
import com.pay.workoutapp.ui.FinishingWorkoutActivity
import com.pay.workoutapp.ui.HistoryActivity

object Router {

    fun navigateToFinish(context: Context) {
        val intent: Intent = Intent(context, FinishingWorkoutActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToExerciseActivity(context: Context) {
        val intent: Intent = Intent(context, ExerciseActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToBMIActivity(context: Context) {
        val intent: Intent = Intent(context, BMIActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToHistoryActivity(context: Context) {
        val intent: Intent = Intent(context, HistoryActivity::class.java)
        context.startActivity(intent)
    }
}