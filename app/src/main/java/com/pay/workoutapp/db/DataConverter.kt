package com.pay.workoutapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    @TypeConverter
    fun fromEntityToString(enityModel: DBEntity?): String? {
        if (enityModel == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<DBEntity?>() {}.type
        return gson.toJson(enityModel, type)
    }

    @TypeConverter
    fun fromStringToEntity(entityString: String?): DBEntity? {
        if (entityString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<DBEntity?>() {}.type
        return gson.fromJson(entityString, type)
    }
}