package com.pay.workoutapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DBEntity::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class BMIDatabase : RoomDatabase() {
    abstract fun getBMIDAO(): BMIDAO?

//    companion object {
//        @Synchronized
//        fun getInstance(bmiDatabase: BMIDatabase, context: Context): BMIDatabase? {
//            if (bmiDatabase.instance == null) {
//                bmiDatabase.instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    BMIDatabase::class.java,
//                    bmiDatabase.DATABASE_NAME
//                ).build()
//            }
//            return bmiDatabase.instance
//        }
//    }

//    companion object {
//        @Volatile
//        private var instance: BMIDatabase? = null
//        private val LOCK = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: buildDatabase(context).also { instance = it }
//        }
//
//        private fun buildDatabase(context: Context) = instance?.DATABASE_NAME?.let {
//            Room.databaseBuilder(
//                context,
//                BMIDatabase::class.java, it
//            ).build()
//        }
//    }

    companion object {
        private val DATABASE_NAME: String = "bmi_info_db"

        // Singleton prevents multiple
        // instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BMIDatabase? = null

        fun getDatabase(context: Context): BMIDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BMIDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}