package com.example.musicplayer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Flow

enum class First(val color:String){
    SILVER("gray"),
    GOLD("gold"),
    PLATINUM("black")
}



fun main(){

    val test = listOf(1,3,4,5,6,7,7)
    repeat(test.size){
        if(test.size-1 == it){
            println("NIGGA")
        }
    }


}