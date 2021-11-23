package com.pay.workoutapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.pay.workoutapp.R
import com.pay.workoutapp.databinding.ActivityBMIBinding
import com.pay.workoutapp.db.DBEntity
import com.pay.workoutapp.utils.Utils
import com.pay.workoutapp.utils.Utils.dateToString
import com.pay.workoutapp.viewmodel.BMIViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


class BMIActivity : AppCompatActivity() {

    private val TAG: String = BMIActivity::class.java.simpleName
    private lateinit var bmiBinding: ActivityBMIBinding
    private lateinit var view: View
    private lateinit var et_weight: AppCompatEditText
    private lateinit var et_height: AppCompatEditText

    private val METRIC_UNITS_VIEW = "METRIC UNITS VIEW"
    private val US_UNITS_VIEW = "US UNITS VIEW"

    private var curentVisibleView: String = METRIC_UNITS_VIEW

    private lateinit var viewModel: BMIViewModel

    private var bmiLabel: String = ""
    private var bmiDescription: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        setupHistoryViewModel()
        addCalculateAction()
    }

    private fun initUI() {
        bmiBinding = ActivityBMIBinding.inflate(layoutInflater)
        view = bmiBinding.root
        setContentView(view)
        setupToolbar()

        et_weight = bmiBinding.etWeight
        et_height = bmiBinding.etHeight
        makeVisibleMetricView()
        setRadioButtonListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(bmiBinding.includeCustomTb.exerciseTb)
        val supportedActionbar: ActionBar? = supportActionBar
        supportedActionbar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        bmiBinding.includeCustomTb.exerciseTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupHistoryViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[BMIViewModel::class.java]
    }

    private fun addCalculateAction() {
        bmiBinding.btCalculate.setOnClickListener {
            val it1 = it
            Log.i(TAG, "addCalculateAction")
            var out: Float
            if (curentVisibleView == METRIC_UNITS_VIEW) {
                out = calculateBMI()
            } else {
                out = calculateBMIWithUSUnits()
            }
            out?.let {
                if (it > 0) {
                    bmiBinding.llBmiOutput.visibility = View.VISIBLE
                    bmiBinding.tvBmiVal.text = it.toString()
                    setBMIDescriptions(it)
                    saveBMIDataToDB(it, it1)
                } else {
                    Toast.makeText(
                        this, R.string.bmi_error_message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun calculateBMI(): Float {
        val height = et_height.text.toString()
        val weight = et_weight.text.toString()
        var out = 0.0.toFloat()
        if (height.isEmpty()) {
            return out
        }
        if (weight.isEmpty()) {
            return out
        }

        var temp = weight.toFloat() / (height.toFloat() * height.toFloat())
        temp *= 10000
        out = temp
        val roundedVal = BigDecimal(out.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN)
        out = roundedVal.toFloat()
        return out
    }

    private fun calculateBMIWithUSUnits(): Float {
        val height_feet = bmiBinding.etHeightFeet.text.toString()
        val height_inches = bmiBinding.etHeightInches.text.toString()
        val weight_lbs = bmiBinding.etWeightUs.text.toString()

        var out = 0.0.toFloat()
        if (height_feet.isEmpty() && height_inches.isEmpty()) {
            return out
        }
        if (weight_lbs.isEmpty()) {
            return out
        }
        var totalHeight = 0.toFloat()
        height_feet?.let {
            if (!it.isEmpty())
                totalHeight = it.toFloat() * 12
        }

        height_inches?.let {
            if (!it.isEmpty())
                totalHeight += it.toFloat()
        }

        var temp = weight_lbs.toFloat() / (totalHeight * totalHeight)
        temp *= 703
        out = temp
        val roundedVal = BigDecimal(out.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN)
        out = roundedVal.toFloat()
        return out
    }

    private fun setBMIDescriptions(bmi: Float) {

        when {
            bmi.compareTo(15f) <= 0 -> {
                bmiLabel = "Very severely underweight"
                bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
            }
            bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0 -> {
                bmiLabel = "Severely underweight"
                bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
            }
            bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0 -> {
                bmiLabel = "Underweight"
                bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
            }
            bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0 -> {
                bmiLabel = "Normal"
                bmiDescription = "Congratulations! You are in a good shape!"
            }
            java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(bmi, 30f) <= 0 -> {
                bmiLabel = "Overweight"
                bmiDescription =
                    "Oops! You really need to take care of your yourself! Workout maybe!"
            }
            bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0 -> {
                bmiLabel = "Obese Class | (Moderately obese)"
                bmiDescription =
                    "Oops! You really need to take care of your yourself! Workout maybe!"
            }
            bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0 -> {
                bmiLabel = "Obese Class || (Severely obese)"
                bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
            }
            else -> {
                bmiLabel = "Obese Class ||| (Very Severely obese)"
                bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
            }
        }
        bmiBinding.tvBmiType.text = bmiLabel
        bmiBinding.tvBmiDescription.text = bmiDescription
    }

    private fun makeVisibleMetricView() {
        curentVisibleView = METRIC_UNITS_VIEW

        bmiBinding.llMatric.visibility = View.VISIBLE
        bmiBinding.llUsUnit.visibility = View.GONE
        bmiBinding.llBmiOutput.visibility = View.GONE

        bmiBinding.etHeight.text?.clear()
        bmiBinding.etWeight.text?.clear()
    }

    private fun makeVisibleUSUNitView() {
        curentVisibleView = US_UNITS_VIEW

        bmiBinding.llMatric.visibility = View.GONE
        bmiBinding.llUsUnit.visibility = View.VISIBLE
        bmiBinding.llBmiOutput.visibility = View.GONE

        bmiBinding.etHeightFeet.text?.clear()
        bmiBinding.etHeightInches.text?.clear()
        bmiBinding.etWeightUs.text?.clear()
    }

    private fun setRadioButtonListener() {
        bmiBinding.rbGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == bmiBinding.meticUnits.id) {
                makeVisibleMetricView()
            } else {
                makeVisibleUSUNitView()
            }
        }
    }

    private fun saveBMIDataToDB(bmiVal: Float, view: View) {
        var entity =
            DBEntity(bmiVal.toString(), Date().dateToString(Utils.DATE_FORMAT), bmiLabel)
        lifecycleScope.launch {
            val out: Long? = viewModel.insertEntity(entity)
            out?.let {
                if (it.compareTo(-1) > 0) {
                    Log.i(TAG, "saveBMIDataToDB: $it")
                    Snackbar.make(
                        this@BMIActivity,
                        view,
                        getString(R.string.successfully_saved),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}