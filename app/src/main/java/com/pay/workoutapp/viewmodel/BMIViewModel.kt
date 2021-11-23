package com.pay.workoutapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pay.workoutapp.db.BMIDAO
import com.pay.workoutapp.db.BMIDatabase
import com.pay.workoutapp.db.BMIRepository
import com.pay.workoutapp.db.DBEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BMIViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG: String = BMIViewModel::class.java.simpleName
    var allBMIEntities: LiveData<List<DBEntity>>? = null
    private var repository: BMIRepository? = null

    init {
        val dao: BMIDAO? = BMIDatabase.getDatabase(application)?.getBMIDAO()
        dao?.let {
            repository = BMIRepository.getInstance(it)
            allBMIEntities = repository?.let {
                it.allNotes
            }
        }
        Log.i(TAG, "Viewmodel Init completed...")
    }

    suspend fun insertEntity(dbEntity: DBEntity): Long? {
        val returnedrepo = viewModelScope.async(Dispatchers.IO) {
            Log.i(TAG, "insertEntity:")
            repository?.let {
                it.insertEntity(dbEntity)
            }
        }
        return returnedrepo.await()
    }

    suspend fun selectEntity(id: Int): LiveData<DBEntity> {
        val result = MutableLiveData<DBEntity>()
        val entity = viewModelScope.async(Dispatchers.IO) {
            repository?.let {
                it.getBMIEntity(id)
            }
        }
        withContext(Dispatchers.Main) {
            result.postValue(entity.await())
        }
        return result
    }

    suspend fun deleteBMIEntity(id: Int): Int? {
        val returnVal = viewModelScope.async(Dispatchers.IO) {
            Log.i(TAG, "deleteBMIEntity:")
            repository?.let {
                it.delete(id)
            }
        }
        return returnVal.await()
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository?.let {
            it.deleteAll()
        }
    }

    // on below line we are creating a new method for updating a note. In this we are
    // calling a update method from our repository to update our note.
    fun updateEntity(bmi_value: String, bmi_description: String, bmi_date: String, bmi_id: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository?.let {
                it.update(bmi_value, bmi_description, bmi_date, bmi_id)
            }
        }
}