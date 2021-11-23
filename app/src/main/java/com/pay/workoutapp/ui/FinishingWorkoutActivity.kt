package com.pay.workoutapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pay.workoutapp.R
import com.pay.workoutapp.databinding.ActivityFinishingWorkoutBinding

class FinishingWorkoutActivity : AppCompatActivity() {
    val TAG: String = FinishingWorkoutActivity::class.java.simpleName

    lateinit var finishingBinding: ActivityFinishingWorkoutBinding
    lateinit var view: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        addActions()
    }

    private fun initUI() {
        finishingBinding = ActivityFinishingWorkoutBinding.inflate(layoutInflater)
        view = finishingBinding.root
        setContentView(view)
        setSupportActionBar(finishingBinding.exerciseTb)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        finishingBinding.exerciseTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addActions() {
        finishingBinding.btnFinish.setOnClickListener {
            Log.i(TAG, "addActions: Finishing the workout...")
            finish()
        }
    }
}