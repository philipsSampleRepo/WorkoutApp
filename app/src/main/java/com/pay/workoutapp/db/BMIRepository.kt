package com.pay.workoutapp.db

import android.content.Context
import android.content.Entity
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CompletableJob

class BMIRepository(private val bmidao: BMIDAO) {

    companion object {
        private var INSTANCE: BMIRepository? = null
        fun getInstance(bmidaoVal: BMIDAO): BMIRepository {
            if (INSTANCE == null) {
                INSTANCE = BMIRepository(
                    bmidao = bmidaoVal
                )
            }
            return INSTANCE as BMIRepository
        }
    }

    val allNotes: LiveData<List<DBEntity>> = bmidao.getAllEntities()

    // on below line we are creating an insert method
    // for adding the note to our database.
     fun insertEntity(entity: DBEntity): Long {
        return bmidao.insertEntity(entity)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
     fun delete(entityID: Int):Int {
        return bmidao.deleteEntity(entityID)
    }

    // on below line we are creating a update method for
    // updating our note from database.
     fun update(bmi_value: String, bmi_description: String, bmi_date: String, bmi_id: Int) {
        bmidao.updateBMIEntity(bmi_date, bmi_value, bmi_description, bmi_id)
    }

     fun deleteAll() {
        bmidao.deleteAllEntities()
    }

     fun getBMIEntity(id: Int): DBEntity? {
        return bmidao.getBMIEntity(id)
    }

}