package com.pay.workoutapp.utils

import com.pay.workoutapp.R
import com.pay.workoutapp.model.Model

class Constants {

    val exerciseList: ArrayList<Model> = ArrayList()

    init {
        initData()
    }

    private fun initData() {

        val ex_jumpingJacks = Model(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
        )

        val ex_wallsitting = Model(
            2,
            "Wall Sitting",
            R.drawable.ic_wall_sit,
            false,
            false
        )
        val ex_pushup = Model(
            3,
            "Pushups",
            R.drawable.ic_push_up,
            false,
            false
        )

        val ex_ab_crunches = Model(
            4,
            "Abbs Crunches",
            R.drawable.ic_abdominal_crunch,
            false,
            false
        )

        val ex_stepup_onto_chair = Model(
            5,
            "Stepup Onto Chair",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )
        val ex_squats = Model(
            6,
            "Squats",
            R.drawable.ic_squat,
            false,
            false
        )

        val ex_tricep_on_chair = Model(
            7,
            "Tricep On Chair",
            R.drawable.ic_triceps_dip_on_chair,
            false,
            false
        )

        val ex_plank = Model(
            8,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )

        val ex_highknee_running = Model(
            9,
            "High Knees Running In Place",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )

        val ex_lungches = Model(
            10,
            "Lunges",
            R.drawable.ic_lunge,
            false,
            false
        )

        val ex_pushup_and_rotation = Model(
            11,
            "Pushup and Rotation",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )

        val ex_side_plank = Model(
            12,
            "Side Plank",
            R.drawable.ic_side_plank,
            false,
            false
        )

        val ex_side_plank1 = Model(
            13,
            "Side Plank1",
            R.drawable.ic_side_plank,
            false,
            false
        )

        val ex_side_plank2 = Model(
            14,
            "Side Plank2",
            R.drawable.ic_side_plank,
            false,
            false
        )
        exerciseList.add(ex_jumpingJacks)
        exerciseList.add(ex_wallsitting)
        exerciseList.add(ex_pushup)
        exerciseList.add(ex_ab_crunches)
        exerciseList.add(ex_stepup_onto_chair)
        exerciseList.add(ex_squats)
        exerciseList.add(ex_tricep_on_chair)
        exerciseList.add(ex_plank)
        exerciseList.add(ex_highknee_running)
        exerciseList.add(ex_lungches)
        exerciseList.add(ex_pushup_and_rotation)
        exerciseList.add(ex_side_plank)
    }
}