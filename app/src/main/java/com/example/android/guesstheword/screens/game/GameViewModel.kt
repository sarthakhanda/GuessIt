package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 100)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 200)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel: ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        private const val DONE = 0L
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L
        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
        // This is the total time of the game
        private const val COUNTDOWN_TIME = 60000L
    }


    private val timer: CountDownTimer

    private val mCurrentTime = MutableLiveData<Long>()
    private val currentTime: LiveData<Long>
        get() = mCurrentTime

    val currentTimeString = Transformations.map(currentTime) {
        time -> DateUtils.formatElapsedTime(time)
    }

    // The current word
    private val mWord = MutableLiveData<String>()
    val word : LiveData<String>
        get() = mWord

    // The current score
    private val mScore = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = mScore

    private lateinit var wordList: MutableList<String>

    private val mGameFinished = MutableLiveData<Boolean>()
    val gameFinished : LiveData<Boolean>
        get() = mGameFinished

    private val mEventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = mEventBuzz

    init {
        resetList()
        nextWord()
        mScore.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                mCurrentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    mEventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                mCurrentTime.value = DONE
                mEventBuzz.value = BuzzType.GAME_OVER
                mGameFinished.value = true
            }
        }

        timer.start()
    }

    private fun resetList() {
        wordList = mutableListOf(
                "Ferrari",
                "Cricket",
                "Basketball",
                "Golden Retriever",
                "Currency",
                "Lion",
                "Chicken",
                "Calendar",
                "iPhone",
                "Couch",
                "Guitar",
                "Villa",
                "Airlines",
                "Zebra",
                "Jelly",
                "Pagani",
                "Dollar",
                "Stocks",
                "Tacos",
                "Burger",
                "Canada"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        mWord.value = wordList.removeAt(0)
    }

    fun onSkip() {
        mScore.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        mScore.value = score.value?.plus(1)
        mEventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishedComplete() {
        mGameFinished.value = false
    }

    fun onBuzzComplete() {
        mEventBuzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}