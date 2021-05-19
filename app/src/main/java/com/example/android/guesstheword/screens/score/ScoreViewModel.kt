package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {

    private val mEventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = mEventPlayAgain

    private val mScore = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = mScore

    init {
        mScore.value = finalScore
    }

    fun onPlayAgain() {
        mEventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        mEventPlayAgain.value = false
    }
}