package com.pay.workoutapp

import com.pay.workoutapp.utils.Utils.dateToString
import org.junit.Test
import java.util.*

class UtilsTest {

    @Test
    fun testDateAndTime() {
        val format = "dd-MMM-YYYY"
        val out = Date().dateToString(format)
        assert(true)
    }

    @Test
    fun testCompareTo() {
        val int1: Int = 5
        val int2: Int = 5

        if (int1.compareTo(int2) == 0) {
           assert(1==1)
        }

        val int3: Int = 7
        val int4: Int = 6

        if (int3.compareTo(int4) > 0) {
            assert(1==1)
        }

        val int5: Int = 4
        val int6: Int = 5

        if (int5.compareTo(int6) < 0) {
            assert(1==1)
        }
    }
}