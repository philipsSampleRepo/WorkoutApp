package com.pay.workoutapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.translationMatrix
import com.pay.workoutapp.databinding.ActivityMainBinding
import com.pay.workoutapp.utils.Router


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        view = mainBinding.root
        setContentView(view)
        initUi()
        launchBMIActivity()
        launchHistoryActivity()
    }

    private fun initUi() {
        mainBinding.llStart.setOnClickListener {
            Log.i(TAG, "initUi: start button clicked...")
            Router.navigateToExerciseActivity(this)
        }
    }

    private fun launchBMIActivity() {
        mainBinding.llBmiCal.setOnClickListener {
            Log.i(TAG, "launchBMIActivity")
            Router.navigateToBMIActivity(this)
        }
    }

    private fun launchHistoryActivity() {
        mainBinding.llHistory.setOnClickListener {
            Log.i(TAG, "launchHistoryActivity")
            Router.navigateToHistoryActivity(this)
        }
    }
}