package com.pay.workoutapp.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bmi_info", indices = [Index(value = ["bmi_id"], unique = true)])
data class DBEntity(
    @PrimaryKey(autoGenerate = true)
    var bmi_id: Int,
    @ColumnInfo(name = "BMI_VAL") var bmi_value: String,
    @ColumnInfo(name = "DATE") var date: String,
    @ColumnInfo(name = "DESCRIPTION") var description: String
) : Parcelable {
    constructor(bmiVal: String, date: String, description: String) : this(
        0,
        bmiVal,
        date,
        description
    )
}