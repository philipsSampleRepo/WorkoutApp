package com.pay.workoutapp.utils

import com.pay.workoutapp.db.DBEntity

interface BMIHistoryItemsListener {
    fun onUpdate(entity: DBEntity, position: Int)
    fun onDelete(entity: DBEntity, position: Int)
}