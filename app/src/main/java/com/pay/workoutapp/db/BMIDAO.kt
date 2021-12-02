package com.pay.workoutapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BMIDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertEntity(user: DBEntity?): Long

    @Query("SELECT * FROM bmi_info")
    fun getAllEntities(): LiveData<List<DBEntity>>

    @Query("SELECT * FROM bmi_info WHERE bmi_id =:bmiID")
    fun getBMIEntity(bmiID: Int): LiveData<DBEntity>?

    @Query("DELETE FROM bmi_info WHERE bmi_id = :bmiID")
    fun deleteEntity(bmiID: Int): Int

    @Query("DELETE  FROM bmi_info")
    fun deleteAllEntities()

    @Query("UPDATE bmi_info SET DATE=:date, BMI_VAL=:bmi_val, DESCRIPTION=:description  WHERE bmi_id = :bmiID")
    fun updateBMIEntity(date: String?, bmi_val: String?, description: String?, bmiID: Int)
}