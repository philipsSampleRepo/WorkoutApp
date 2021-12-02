package com.pay.workoutapp.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pay.workoutapp.R
import com.pay.workoutapp.adapters.HistoryListAdapter
import com.pay.workoutapp.databinding.ActivityHistoryBinding
import com.pay.workoutapp.databinding.CustomAlertDialogBinding
import com.pay.workoutapp.databinding.UpdateDialogInputBoxBinding
import com.pay.workoutapp.db.DBEntity
import com.pay.workoutapp.utils.BMIHistoryItemsListener
import com.pay.workoutapp.viewmodel.BMIViewModel
import kotlinx.coroutines.launch
import java.util.*

class HistoryActivity : AppCompatActivity(), BMIHistoryItemsListener {
    private val TAG: String = HistoryActivity::class.java.simpleName
    private lateinit var historyActivity: ActivityHistoryBinding
    private lateinit var view: View
    private lateinit var viewModel: BMIViewModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var bmiItemsAdapter: HistoryListAdapter
    private lateinit var updateRecordCustomDialog: Dialog
    private lateinit var updateRecordCustomDialogBinding: UpdateDialogInputBoxBinding
    private lateinit var deleteRecordCustomDialogBinding: CustomAlertDialogBinding
    private lateinit var deleteRecordCustomDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        setupHistoryViewModel()
        initUpdateRecordDialogUI()
        initDeleteRecordDialog()
    }

    private fun initUpdateRecordDialogUI() {
        updateRecordCustomDialog =
            Dialog(this@HistoryActivity, R.style.Dialog_Theme)
        updateRecordCustomDialogBinding = UpdateDialogInputBoxBinding.inflate(layoutInflater)
        val view: View = updateRecordCustomDialogBinding.root
        updateRecordCustomDialog.setContentView(view)
    }

    private fun initDeleteRecordDialog() {
        deleteRecordCustomDialog =
            Dialog(this@HistoryActivity, R.style.Dialog_Theme)
        deleteRecordCustomDialogBinding = CustomAlertDialogBinding.inflate(layoutInflater)
        val view: View = deleteRecordCustomDialogBinding.root
        deleteRecordCustomDialog.setContentView(view)

        deleteRecordCustomDialogBinding.tvQuestion.text = getString(R.string.delete_record)
        deleteRecordCustomDialogBinding.tvDescription.text =
            getString(R.string.delete_record_description)
    }

    private fun initUI() {
        historyActivity = ActivityHistoryBinding.inflate(layoutInflater)
        view = historyActivity.root
        setContentView(view)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(historyActivity.includeCustomTb.exerciseTb)
        val supportActionBar: ActionBar? = supportActionBar
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        historyActivity.includeCustomTb.exerciseTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupHistoryViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[BMIViewModel::class.java]

        setupHistoryData()
        setupRecyclerView()
    }

    private fun setupHistoryData() {
        lifecycleScope.launch {
            viewModel.getAllBMIEntities()?.let {
                it.observe(this@HistoryActivity, Observer { list ->
                    Log.i(TAG, "setupViewModel: ${list.size}")
                    if (list.isEmpty()) {
                        historyActivity.tvNoHistory.visibility = View.VISIBLE
                    } else {
                        historyActivity.tvNoHistory.visibility = View.GONE
                        bmiItemsAdapter.setItems(list)
                    }
                })
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = historyActivity.rvHistory
        recyclerView.layoutManager = LinearLayoutManager(this)
        bmiItemsAdapter = HistoryListAdapter(this, this)
        recyclerView.adapter = bmiItemsAdapter
    }

    override fun onUpdate(entity: DBEntity, position: Int) {
        Log.i(TAG, "onUpdate: ")
        customDialogForRecordUpdate(entity, position)
    }

    override fun onDelete(entity: DBEntity, position: Int) {
        Log.i(TAG, "onDelete: ")
        deleteRecordCustomDialogBinding.btnYes.setOnClickListener {
            Log.i(TAG, "onDelete: ")
            deleteRecord(entity.bmi_id)
            bmiItemsAdapter.removeItem(position)
            deleteRecordCustomDialog.dismiss()
        }
        deleteRecordCustomDialogBinding.btnNo.setOnClickListener {
            deleteRecordCustomDialog.dismiss()
        }
        deleteRecordCustomDialog.show()
    }

    private fun deleteRecord(entity: Int) {
        lifecycleScope.launch {
            val out: Int? = viewModel.deleteBMIEntity(entity)
            out?.let {
                if (it.compareTo(-1) > 0) {
                    Log.i(TAG, "deleteRecord: $it")
                    Snackbar.make(
                        this@HistoryActivity,
                        view,
                        getString(R.string.successfully_deleted),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun customDialogForRecordUpdate(entity: DBEntity, position: Int) {
        updateRecordCustomDialogBinding.etBmi.setText(entity.bmi_value)
        updateRecordCustomDialogBinding.etDesc.setText(entity.description)

        updateRecordCustomDialogBinding.btnOk.setOnClickListener {
            Log.i(TAG, "updateItemCustomDialog Ok button")

            val bmi_val = updateRecordCustomDialogBinding.etBmi.text.toString()
            val bmi_des = updateRecordCustomDialogBinding.etDesc.text.toString()

            if (validateUpdateRecordEntry(bmi_val, bmi_des)) {
                bmiItemsAdapter.updateItem(entity, position)
                viewModel.updateEntity(bmi_val, bmi_des, entity.date, entity.bmi_id)
                getUpdateEntity(entity)
            } else {
                Toast.makeText(this@HistoryActivity, R.string.update_error, Toast.LENGTH_LONG)
                    .show()
            }
            updateRecordCustomDialog.dismiss()
        }
        updateRecordCustomDialogBinding.btnCancel.setOnClickListener {
            Log.i(TAG, "updateItemCustomDialog Cancel button")
            updateRecordCustomDialog.dismiss()
        }
        updateRecordCustomDialog.show()
    }

    private fun validateUpdateRecordEntry(bmi_val: String, bmi_des: String): Boolean {
        if (bmi_val.isEmpty() || bmi_des.isEmpty()) {
            return false
        }
        return true;
    }

    private fun getUpdateEntity(entity: DBEntity) {
        lifecycleScope.launch {
            viewModel.selectEntity(entity.bmi_id)?.observe(this@HistoryActivity, {
                it?.let {
                    Log.i(TAG, "customDialogForRecordUpdate: ${it.bmi_value}")
                }
            })
        }
    }
}