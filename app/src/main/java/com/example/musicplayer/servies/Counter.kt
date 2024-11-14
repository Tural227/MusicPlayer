package com.example.musicplayer.servies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Counter: ViewModel() {
    var isRunning = true
    val counterState = MutableStateFlow(0)




    fun startCounter(){
        viewModelScope.launch {
            while (isRunning) {
                counterState.update {
                    it + 1 }
                delay(3000)
            }
        }
    }
    fun stopCounter(){
        isRunning = false
    }

}