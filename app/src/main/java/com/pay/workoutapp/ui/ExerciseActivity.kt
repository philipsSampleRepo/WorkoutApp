package com.pay.workoutapp.ui

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pay.workoutapp.R
import com.pay.workoutapp.databinding.ActivityExcerciseBinding
import com.pay.workoutapp.databinding.CustomDialogBinding
import com.pay.workoutapp.model.Model
import com.pay.workoutapp.utils.Constants
import com.pay.workoutapp.adapters.ExerciseStatusAdapter
import com.pay.workoutapp.utils.Router
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val TAG = ExerciseActivity::class.java.simpleName
    private var exerciseBinding: ActivityExcerciseBinding? = null
    private lateinit var view: View
    private var countDownTimer: CountDownTimer? = null
    private var countDownTimerExercise: CountDownTimer? = null

    private val TOTAL_TIME: Int = 2
    private val TOTAL_TIME_EXERCISE = 3

//    private val TOTAL_TIME: Int = 10
//    private val TOTAL_TIME_EXERCISE = 30

    private val TOTAL_TIME_INTERVAL_MILIS: Long = TOTAL_TIME * 1000.toLong()
    private val TOTAL_TIME_EXERCISE_MILIS: Long = TOTAL_TIME_EXERCISE * 1000.toLong()
    private val COUNT_DOWN_INTERVAL: Long = 1000

    private var progress_rest: Int = 0

    private var exercise_List: ArrayList<Model>? = null
    private var current_position = -1

    private lateinit var tts: TextToSpeech
    private var mplayer: MediaPlayer? = null

    private lateinit var exerciseAdapter: ExerciseStatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exerciseBinding = ActivityExcerciseBinding.inflate(layoutInflater)
        view = exerciseBinding!!.root
        setContentView(view)
        initTTS()
        init_Exercise_List()
        initAdapter()
        setToolbar()
        startRestProgressBar()
    }

    private fun initTTS() {
        tts = TextToSpeech(this, this)
    }

    private fun initAdapter() {
        exercise_List.let {
            exerciseAdapter = ExerciseStatusAdapter(it ?: ArrayList(), this)
            exerciseBinding?.recyclerview?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            exerciseBinding?.recyclerview?.adapter = exerciseAdapter
        }
    }

    private fun init_Exercise_List() {
        exercise_List = Constants().exerciseList
        exercise_List.let {
            Log.i(TAG, "init_Exercise_List: ${it?.size}")
        }
    }

    private fun setToolbar() {
        setSupportActionBar(exerciseBinding?.exerciseTb)
        val supportActionBar: ActionBar? = supportActionBar
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        exerciseBinding?.exerciseTb?.setNavigationOnClickListener {
            customDialogForBackButton()
        }
    }

    private fun setNextExerciseDetails() {
        exerciseBinding?.tvUpcomingExercise?.text = getNextExercise()
    }

    private fun getNextExercise(): String {
        var name = ""
        exercise_List?.let {
            if (current_position + 1 < it.size) {
                val model: Model? = it[current_position + 1]
                model?.let {
                    name = it.name
                }
            }
        }
        return name
    }

    private fun hideViewFl() {
        exerciseBinding?.flExercise?.visibility = View.GONE
        exerciseBinding?.ivExercise?.visibility = View.GONE
    }

    private fun showViewFl() {
        exerciseBinding?.flExercise?.visibility = View.VISIBLE
        exerciseBinding?.ivExercise?.visibility = View.VISIBLE
    }

    private fun hideResetProgressWidgets() {
        exerciseBinding?.flStart?.visibility = View.GONE
        exerciseBinding?.tvUpcomingLbl?.visibility = View.GONE
        exerciseBinding?.tvUpcomingExercise?.visibility = View.GONE
    }

    private fun showResetProgressWidgets() {
        exerciseBinding?.flStart?.visibility = View.VISIBLE
        exerciseBinding?.tvUpcomingLbl?.visibility = View.VISIBLE
        exerciseBinding?.tvUpcomingExercise?.visibility = View.VISIBLE
    }

    private fun setRestProgress() {
        showResetProgressWidgets()
        setNextExerciseDetails()
        hideViewFl()

        exerciseBinding?.tvMessage?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.toFloat())
        exerciseBinding?.tvMessage?.text = getText(R.string.get_ready)
        exerciseBinding?.progressBar?.progress = progress_rest

        countDownTimer = object : CountDownTimer(TOTAL_TIME_INTERVAL_MILIS, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                progress_rest++
                val progress = TOTAL_TIME - progress_rest
                exerciseBinding?.progressBar?.progress = progress
                exerciseBinding?.tvTimer?.text = progress.toString()
            }

            override fun onFinish() {
                Log.i(TAG, "onFinish: Rest is over...")
                current_position++
                startProgressBarExercise()
                updateExerciseSelectedState()
            }
        }.start()
    }

    private fun setExerciseProgress() {
        hideResetProgressWidgets()
        showViewFl()
        exerciseBinding?.tvMessage?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.toFloat());
        progress_rest = 0
        exerciseBinding?.progressBarExercise?.progress = progress_rest
        countDownTimerExercise =
            object : CountDownTimer(TOTAL_TIME_EXERCISE_MILIS, COUNT_DOWN_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    progress_rest++
                    val progress = TOTAL_TIME_EXERCISE - progress_rest
                    exerciseBinding?.progressBarExercise?.progress = progress
                    exerciseBinding?.tvTimerExercise?.text = progress.toString()
                }

                override fun onFinish() {
                    updateExerciseFinishedState()
                    exercise_List?.let {
                        if (current_position + 1 < it.size) {
                            startMediaPlayer()
                            startRestProgressBar()
                            Log.i(TAG, "onFinish: Exercise is over")
                        } else {
                            finish()
                            Router.navigateToFinish(this@ExerciseActivity)
                        }
                    }
                }
            }.start()
    }

    private fun updateExerciseFinishedState() {
        exercise_List?.let {
            if (current_position > -1) {
                if (current_position < (it.size)) {
                    exercise_List?.get(current_position)?.isSelected = false
                    exercise_List?.get(current_position)?.isCompleted = true
                    exerciseAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateExerciseSelectedState() {
        exercise_List?.let {
            if (it != null) {
                if (current_position > -1 && current_position < (it.size)) {
                    exercise_List?.get(current_position)?.isSelected = true
                    exerciseAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun startMediaPlayer() {
        try {
            mplayer = MediaPlayer.create(applicationContext, R.raw.press_start)
            mplayer?.isLooping = false
            mplayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "startMediaPlayer: ${e.message}")
        }
    }

    private fun startProgressBarExercise() {

        if (countDownTimerExercise != null) {
            countDownTimerExercise?.let {
                it.cancel()
                progress_rest = 0
            }
        }
        exercise_List?.let {
            if (current_position <= it.size - 1) {
                val temp: Model? = it.get(current_position)
                temp?.let {
                    exerciseBinding?.ivExercise?.setImageResource(it.image)
                    exerciseBinding?.tvMessage?.text =
                        it.name
                    speakout(it.name)
                    setExerciseProgress()
                }
            }
        }
    }

    private fun startRestProgressBar() {
        if (countDownTimer != null) {
            countDownTimer?.let {
                it.cancel()
                progress_rest = 0
            }
        }
        setRestProgress()
    }

    override fun onDestroy() {
        if (countDownTimer != null) {
            countDownTimer?.let {
                it.cancel()
                progress_rest = 0
            }
        }

        if (countDownTimerExercise != null) {
            countDownTimerExercise?.let {
                it.cancel()
                progress_rest = 0
            }
        }

        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }

        if (mplayer != null) {
            mplayer?.stop()
            mplayer?.release()
        }
        if (exerciseBinding != null) {
            exerciseBinding = null
        }
        super.onDestroy()
    }

    private fun speakout(text: String) {
        tts.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                it.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            } else {
                Log.i(TAG, "speakout: not supported to this OS version")
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val res = tts.setLanguage(Locale.US)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i(TAG, "onInit: Language is not supported...")
            }
        } else {
            Log.i(TAG, "onInit: init failed...")
        }
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
        val view: View = customDialogBinding.root
        customDialog.setContentView(view)
        customDialogBinding.btnYes.setOnClickListener {
            Log.i(TAG, "customDialogForBackButton: Yes button")
            customDialog.dismiss()
            finish()
        }
        customDialogBinding.btnNo.setOnClickListener {
            Log.i(TAG, "customDialogForBackButton: No button")
            customDialog.dismiss()
        }
        customDialog.show()
    }
}